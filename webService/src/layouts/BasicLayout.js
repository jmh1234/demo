import React from 'react';
import {Layout, message, notification} from 'antd';
import DocumentTitle from 'react-document-title';
import {connect} from 'dva';
import {Redirect, Route, routerRedux, Switch} from 'dva/router';
import {ContainerQuery} from 'react-container-query';
import classNames from 'classnames';
import pathToRegexp from 'path-to-regexp';
import {enquireScreen, unenquireScreen} from 'enquire-js';
import GlobalHeader from '../components/GlobalHeader';
import SiderMenu from '../components/SiderMenu';
import NotFound from '../routes/Exception/404';
import {getRoutes} from '../utils/utils';
import Authorized from '../utils/Authorized';
import {getMenuData} from '../common/menu';
import logo from '../assets/emerald.png';
import request from '../utils/request';

const {Content, Header, Footer} = Layout;
const {AuthorizedRoute} = Authorized;

/**
 * 获取面包屑映射
 * @param {Object} menuData 菜单配置
 * @param {Object} routerData 路由配置
 */
const getBreadcrumbNameMap = (menuData, routerData) => {
  const result = {};
  const childResult = {};
  for (const i of menuData) {
    if (!routerData[i.path]) {
      result[i.path] = i;
    }
    if (i.children) {
      Object.assign(childResult, getBreadcrumbNameMap(i.children, routerData));
    }
  }
  return Object.assign({}, routerData, result, childResult);
};

const query = {
  'screen-xs': {
    maxWidth: 575,
  },
  'screen-sm': {
    minWidth: 576,
    maxWidth: 767,
  },
  'screen-md': {
    minWidth: 768,
    maxWidth: 991,
  },
  'screen-lg': {
    minWidth: 992,
    maxWidth: 1199,
  },
  'screen-xl': {
    minWidth: 1200,
  },
};

let isMobile;
enquireScreen(b => {
  isMobile = b;
});

class BasicLayout extends React.Component {
  state = {
    isMobile,
    eventSource: null,
    flag: false,
    num: 0,
    res: null,
  };

  componentDidMount() {
    this.enquireHandler = enquireScreen(mobile => {
      this.setState({
        isMobile: mobile,
      });
    });
    this.props.dispatch({
      type: 'user/fetchCurrent',
    });
    request('/api/unredMessage')
      .then(res => {
        this.setState({
          num: res
        });
      })
  }

  shouldComponentUpdate() {
    return true;
  }

  componentWillUnmount() {
    unenquireScreen(this.enquireHandler);
    if (this.state.eventSource) {
      this.state.eventSource.close();
      window.sessionStorage.removeItem('notices');
    }
  }

  getPageTitle() {
    const {routerData, location} = this.props;
    const {pathname} = location;
    let title = 'Surprised';
    let currRouterData = null;
    Object.keys(routerData).forEach(key => {
      if (pathToRegexp(key).test(pathname)) {
        currRouterData = routerData[key];
      }
    });
    if (currRouterData && currRouterData.name) {
      title = `${currRouterData.name} - Ant Design Pro`;
    }
    return title;
  }

  handleMenuCollapse = collapsed => {
    this.props.dispatch({
      type: 'global/changeLayoutCollapsed',
      payload: collapsed,
    });
  };
  handleNoticeClear = type => {
    message.success(`清空了${type}`);
    this.props.dispatch({
      type: 'global/clearNotices',
      payload: type,
    });
  };
  handleNoticeClick = (key) => {
    this.props.dispatch(routerRedux.push('/message'));
    notification.close(key);
  };

  handleMenuClick = ({key}) => {
    if (key === 'logout') {
      request('/api/loginout')
        .then(res => {
          if (res.status) {
            window.localStorage.removeItem('token');
            window.localStorage.removeItem('authorities');
            this.props.dispatch(routerRedux.push('/user/login'));
          } else {
            message.error(res.msg);
          }
        })
        .catch(error => {
          console.log(error);
        });
    } else if (key === 'user') {
      request(`api/user/getCurrentUser`)
        .then(res => {
          this.props.dispatch({
            type: 'users/toLinkDetail',
            payload: {
              id: res.id,
              showType: 0,
            },
          });
        })
    }
  };

  handleNoticeVisibleChange = visible => {
    if (visible) {
      this.props.dispatch({
        type: 'global/fetchNotices',
      });
    }
  };

  render() {
    /**
     * 根据菜单取得重定向地址.
     */
    const redirectData = [];
    const getRedirect = item => {
      if (item && item.children) {
        if (item.children[0] && item.children[0].path) {
          redirectData.push({
            from: `${item.path}`,
            to: `${item.children[0].path}`,
          });
          item.children.forEach(children => {
            getRedirect(children);
          });
        }
      }
    };
    getMenuData().forEach(getRedirect);

    const {
      currentUser,
      collapsed,
      fetchingNotices,
      routerData,
      match,
      location,
    } = this.props;
    let bashRedirect = '/user/login';
    const layout = (
      <Layout>
        <SiderMenu
          logo={logo}
          Authorized={Authorized}
          menuData={getMenuData()}
          collapsed={collapsed}
          location={location}
          isMobile={this.state.isMobile}
          onCollapse={this.handleMenuCollapse}
        />
        <Layout>
          <Header style={{padding: 0, backgroundColor: "#000000"}}>
            <GlobalHeader
              logo={logo}
              currentUser={currentUser}
              fetchingNotices={fetchingNotices}
              notices={this.state.notices}
              num={this.state.res === null ? this.state.num : this.state.res}
              collapsed={collapsed}
              isMobile={this.state.isMobile}
              onNoticeClear={this.handleNoticeClear}
              onNoticeClick={() => this.handleNoticeClick()}
              onCollapse={this.handleMenuCollapse}
              onMenuClick={this.handleMenuClick}
              onNoticeVisibleChange={this.handleNoticeVisibleChange}
              dispatch={this.props.dispatch}
            />
          </Header>
          <Content style={{margin: '24px 24px 0', height: '100%'}}>
            <Switch>
              {redirectData.map(item => (
                <Redirect key={item.from} exact from={item.from} to={item.to}/>
              ))}
              {getRoutes(match.path, routerData).map(item => (
                <AuthorizedRoute
                  key={item.key}
                  path={item.path}
                  component={item.component}
                  exact={item.exact}
                  authority={item.authority}
                  redirectPath="/exception/403"
                />
              ))}
              <Redirect exact from="/" to={bashRedirect}/>
              <Route render={NotFound}/>
            </Switch>
          </Content>
          <Footer style={{padding: 0}}>
          </Footer>
        </Layout>
      </Layout>
    );
    this.state.flag = false;
    return (
      <DocumentTitle title={this.getPageTitle()}>
        <ContainerQuery query={query}>
          {params => <div className={classNames(params)}>{layout}</div>}
        </ContainerQuery>
      </DocumentTitle>
    );
  }
}

export default connect(({user, global, loading}) => ({
  currentUser: user.currentUser,
  fetchingNotices: loading.effects['global/fetchNotices'],
}))(BasicLayout);
