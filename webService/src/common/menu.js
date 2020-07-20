import {isUrl} from '../utils/utils';

function menuFormatterDriver() {
  const menuData = [
    {
      name: '账户',
      icon: 'user',
      path: 'user',
      authority: 'guest',
      children: [
        {
          name: '登录',
          path: 'login',
        },
      ],
    },
  ];
  return menuFormatter(menuData);
}

function menuFormatter(data, parentPath = '/') {
  if (window.localStorage.getItem('token') !== null) {
    const authorities = JSON.parse(window.localStorage.getItem('authorities'));

    let menuData = [];

    if (authorities === null) {
      return menuData;
    }

    data.map(item => {
      let {path} = item;
      let parPath = parentPath.replace('/', '').replace('/', '');

      if (!isUrl(path)) {
        path = parentPath + item.path;
      }
      const result = {
        ...item,
        path,
      };

      if (parPath === 'manage' || item.path === 'manage') {
        if (item.children && authorities[item.path]) {
          result.children = menuFormatter(item.children, `${parentPath}${item.path}/`);
          menuData.push(result);
        } else {
          if (
            authorities[parPath] &&
            authorities[parPath][item.path] &&
            ((item.path === 'dataAnalysis' &&
              Object.keys(authorities[parPath][item.path]).length > 0) ||
              authorities[parPath][item.path].show)
          ) {
            menuData.push(result);
          }
        }
      } else if (parPath === 'custom' || item.path === 'custom') {
        if (!!authorities.viewCustom) {
          if (item.children) {
            result.children = menuFormatter(item.children, `${parentPath}${item.path}/`);
            menuData.push(result);
          } else {
            menuData.push(result);
          }
        }
      } else if (parPath === 'task' || item.path === 'task') {
        if (!!authorities.viewTask) {
          if (item.children) {
            result.children = menuFormatter(item.children, `${parentPath}${item.path}/`);
            menuData.push(result);
          } else {
            menuData.push(result);
          }
        }
      } else if (parPath === 'project' || item.path === 'project') {
        if (!!authorities.viewProject) {
          if (item.children) {
            result.children = menuFormatter(item.children, `${parentPath}${item.path}/`);
            menuData.push(result);
          } else {
            menuData.push(result);
          }
        }
      } else if (parentPath === '/Fault/' || item.path === 'fault') {
        if (!!authorities.viewBug) {
          if (item.children) {
            result.children = menuFormatter(item.children, `${parentPath}${item.path}/`);
            menuData.push(result);
          } else {
            menuData.push(result);
          }
        }
      } else if (parentPath === '/dashboard/' || item.path === 'dashboard') {
        menuData.push(result);
      } else if (parentPath === '/message/' || item.path === 'message') {
        menuData.push(result);
      } else if (parentPath === '/library/' || item.path === 'library') {
        if (!!authorities.viewArcInfo || !!authorities.viewFuncLibAndCaseLib) {
          menuData.push(result);
        }
      } else if (parentPath === '/dataAnalysis/' || item.path === 'dataAnalysis') {
        if (!!authorities.viewAnalysis) {
          menuData.push(result);
        }
      } else if (parentPath === '/users/' || item.path === 'users') {
        if (!!authorities.viewUsers) {
          menuData.push(result);
        }
      } else if (parentPath === '/role/' || item.path === 'role') {
        if (!!authorities.viewRole) {
          menuData.push(result);
        }
      } else if (parentPath === '/setting/' || item.path === 'setting') {
        if (item.children && authorities[item.path]) {
          result.children = menuFormatter(item.children, `${parentPath}${item.path}/`);
          menuData.push(result);
        } else {
          if (
            authorities[parPath] &&
            authorities[parPath][item.path] &&
            authorities[parPath][item.path].show
          ) {
            menuData.push(result);
          } else if (
            item.path === 'senior' &&
            authorities[parPath] &&
            authorities[parPath][item.path] &&
            (authorities[parPath][item.path].showScript ||
              authorities[parPath][item.path].showEmail)
          ) {
            menuData.push(result);
          }
        }
      }
      else if (parentPath === '/resource/' || item.path === 'resource') {
        if (!!authorities.viewTestResType) {
          if (item.children) {
            result.children = menuFormatter(item.children, `${parentPath}${item.path}/`);
            menuData.push(result);
          } else {
            menuData.push(result);
          }
        }
      }
    });

    return menuData;
  } else {
    return [];
  }
}

export const getMenuData = menuFormatterDriver;
