import React from 'react';
import {Link, Redirect, Route, Switch} from 'dva/router';
import DocumentTitle from 'react-document-title';
import {Col, Row} from 'antd';
import styles from './UserLayout.less';
import logo from '../assets/favicon.png';
import {getRoutes} from '../utils/utils';

class UserLayout extends React.PureComponent {
  getPageTitle() {
    const {routerData, location} = this.props;
    const {pathname} = location;
    let title = 'Surprised';
    if (routerData[pathname] && routerData[pathname].name) {
      title = `${routerData[pathname].name} - Surprised`;
    }
    return title;
  }

  render() {
    const {routerData, match} = this.props;
    return (
      <DocumentTitle title={this.getPageTitle()}>
        <div className={styles.container}>
          <div className={styles.content}>
            <div className={styles.top}>
              <div className={styles.header}>
                <Row>
                  <Col span={11} style={{textAlign: 'right'}}>
                    <img alt="logo" className={styles.logo} src={logo}/>
                  </Col>
                  <Col span={13} style={{textAlign: 'left'}}>
                    <Row style={{height: 40}}>
                      <span className={styles.title}>Surprised</span>
                    </Row>
                    <Row style={{height: 33}}>
                      <span className={styles.hiltest}>给你的一个小小的惊喜</span>
                    </Row>
                  </Col>
                </Row>
              </div>
              <div className={styles.desc}/>
            </div>
            <Switch>
              {getRoutes(match.path, routerData).map(item => (
                <Route
                  key={item.key}
                  path={item.path}
                  component={item.component}
                  exact={item.exact}
                />
              ))}
              <Redirect exact from="/user" to="/user/login"/>
            </Switch>
          </div>
        </div>
      </DocumentTitle>
    );
  }
}

export default UserLayout;
