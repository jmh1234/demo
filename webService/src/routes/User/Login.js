import React from 'react';
import {connect} from 'dva';
import {Link, routerRedux} from 'dva/router';
import {Alert, message} from 'antd';
import Login from 'components/Login';
import styles from './Login.less';
import request from '../../utils/request';

const {UserName, Password, Submit} = Login;

@connect(({login, loading, global}) => ({
  login,
  submitting: loading.effects['login/login'],
  global,
}))

export default class LoginPage extends React.Component {
  state = {
    type: 'account',
    autoLogin: true,
  };

  componentWillMount() {
    if (window.localStorage.getItem('token')) {
      request(`/api/validateUser?token=${window.localStorage.getItem('token')}`).then(res => {
        if (res && res.status) {
          this.props.dispatch(routerRedux.push('/dashboard'));
        }
      });
    }
  }

  onTabChange = type => {
    this.setState({type});
  };

  handleSubmit = (err, values) => {
    if (!err) {
      request('/api/login', {
        method: 'POST',
        body: values,
      })
        .then(res => {
          if (res.status) {
            window.localStorage.setItem('token', res.msg);
            window.localStorage.setItem('authorities', JSON.stringify(res.authorities));
            this.props.dispatch({
              type: 'global/putAuthorities',
              payload: res.authorities,
            });
            this.props.dispatch(routerRedux.push('/dashboard'));
          } else {
            message.error(res.msg);
          }
        })
        .catch(e => {
          console.log(e);
        });
    }
  };

  renderMessage = content => {
    return <Alert style={{marginBottom: 24}} message={content} type="error" showIcon/>;
  };

  render() {
    const {login, submitting} = this.props;
    const {type} = this.state;
    return (
      <div className={styles.main}>
        <Login defaultActiveKey={type} onTabChange={this.onTabChange} onSubmit={this.handleSubmit}>
          <div style={{marginTop: 150}}>
            {login.status === 'error' &&
            login.type === 'account' &&
            !login.submitting &&
            this.renderMessage('账户或密码错误')}
            <UserName name="username" className={styles.noBorder} placeholder="输入用户名"/>
            <Password name="password" className={styles.noBorder} placeholder="输入密码"/>
          </div>
          <Submit loading={submitting}
                  style={{
                    background: '#75BF76',
                    borderRadius: '59px'
                  }}>登录</Submit>
        </Login>
      </div>
    );
  }
}
