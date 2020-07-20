import request from '../utils/request';
import {routerRedux} from "dva/router";
import {message} from "antd/lib/index";

export async function query() {
  return request('/api/users');
}

export async function queryCurrent() {
  return request('/api/currentUser');
}

export async function loginOut() {
   return request('/api/loginout')
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
}

export async function getAuthority() {
  return request(`/api/authority`)
}
