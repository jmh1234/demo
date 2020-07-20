import React from 'react';
import groupBy from 'lodash/groupBy';
import NoticeIcon from '../NoticeIcon';
import {Link, routerRedux} from 'dva/router';
import Debounce from 'lodash-decorators/debounce';

import { notification } from "antd/lib/index";
import { Menu, Icon, Spin, Tag, Dropdown, Divider, Badge } from 'antd';

import styles from './index.less';

export default class GlobalHeader extends React.Component {
  state = {
    eventSource: null,
    res: null
  };

  componentWillUnmount() {
    this.triggerResizeEvent.cancel();
    if(this.state.eventSource){
      this.state.eventSource.close();
      window.sessionStorage.removeItem('notices');
    }
  }

  componentDidMount () {

    /*  TODO
    const eventSource = new EventSource(`/api/push?token=${window.localStorage.getItem('token')}`);

    const notices = [];
    eventSource.open = e =>{

    };
    eventSource.onmessage = e =>{
      let obj = JSON.parse(e.data);

      const key = `open${Date.now()}`;

      let list = window.sessionStorage.getItem('notices');
      if(obj.status === 0 &&( list === null || (list.length > 0 && !list.includes(e.data)))){
        notification.open({
          key,
          message: <a onClick={()=>this.handleNoticeClick(key)}>{obj.title}</a>,
          description: <a onClick={()=>this.handleNoticeClick(key)}>{obj.content}</a>,
        });
        notices.push(e.data);
        window.sessionStorage.setItem('notices', notices);
      }
      this.setState({
        res: parseInt(e.lastEventId)
      })
    };

    eventSource.error = e => {
      console.log(e);
    }

    this.setState({
      eventSource: eventSource
    })
    */
  }

  handleNoticeClick = (key) => {
    this.props.dispatch(routerRedux.push('/message'));
    notification.close(key);
  }

  getNoticeData() {
    const { notices = [] } = this.props;
    if (notices.length === 0) {
      return {};
    }
    const newNotices = notices.map(notice => {
      const newNotice = { ...notice };

      if (newNotice.id) {
        newNotice.key = newNotice.id;
      }
      if (newNotice.content && newNotice.status) {
        const color = {
          todo: '',
          processing: 'blue',
          urgent: 'red',
          doing: 'gold',
        }[newNotice.status];
        newNotice.extra = (
          <Tag color={color} style={{ marginRight: 0 }}>
            {newNotice.extra}
          </Tag>
        );
      }
      return newNotice;
    });
    return groupBy(newNotices, 'type');
  }

  toggle = () => {
    const { collapsed, onCollapse } = this.props;
    onCollapse(!collapsed);
    this.triggerResizeEvent();
  };
  /* eslint-disable*/
  @Debounce(600)
  triggerResizeEvent() {
    const event = document.createEvent('HTMLEvents');
    event.initEvent('resize', true, false);
    window.dispatchEvent(event);
  }
  render() {
    const {
      currentUser = {},
      collapsed,
      fetchingNotices,
      isMobile,
      logo,
      onNoticeVisibleChange,
      onMenuClick,
      onNoticeClear,
      notices,
      num,
    } = this.props;
    const menu = (
      <Menu className={styles.menu} selectedKeys={[]} onClick={onMenuClick}>
        <Menu.Item key="user">
        <Icon type="user" />个人中心
        </Menu.Item>
        <Menu.Item key="logout">
          <Icon type="logout" />退出登录
        </Menu.Item>
      </Menu>
    );

    return (
      <div className={styles.header}>
        {isMobile && [
          <Link to="/" className={styles.logo} key="logo">
            <img src={logo} alt="logo" width="32" />
          </Link>,
          <Divider type="vertical" key="line" />,
        ]}
        <Icon
          className={styles.trigger}
          type={collapsed ? 'menu-unfold' : 'menu-fold'}
          onClick={this.toggle}
        />
        <div className={styles.right}>
          <Badge>
            <Icon type="hdd" />
          </Badge>
          <NoticeIcon
            className={styles.action}
            count={this.state.res}
            onClick={this.props.onNoticeClick}
            popupAlign={{ offset: [20, -16] }}
          >
          </NoticeIcon>
          {currentUser.name ? (
            <Dropdown overlay={menu}>
              <span className={`${styles.action} ${styles.account}`}>
                <span style={{ paddingRight: '5px', fontSize: 16 }}>
                  <Icon type="user" />
                </span>
                <span className={styles.name}>{currentUser.name}</span>
              </span>
            </Dropdown>
          ) : (
            <Spin size="small" style={{ marginLeft: 8 }} />
          )}
        </div>
      </div>
    );
  }
}
