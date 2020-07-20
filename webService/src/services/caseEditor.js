
const electron = !!window.require?window.require('electron'):{};
const ipcRenderer = electron.ipcRenderer;

import request from '../utils/request';

export async function exportDataToConfig(taskNumId, data) {
    return request('/api/task/setTarTestCases', {
        method: 'POST',
        body: {
          taskId: taskNumId,
          data: data,
        },
      });
}

export async function importDataFromConfig(taskNumId) {
    return request('/api/task/getTarTestCases', {
        method: 'POST',
        body: {
          taskId: taskNumId,
        },
      });
}

export async function deleteDataFromConfig(taskNumId) {
    return ipcRenderer.invoke('deleteDataFromConfig', taskNumId);
}

export async function importDataFromExcel(taskNumId, filePath) {
    return ipcRenderer.invoke('importDataFromExcel', taskNumId, filePath);
}

export async function exportDataToPack(taskNumId, data) {
    return request('/api/task/setAnddownloadTarTestCasesInPack', {
        method: 'POST',
        body: {
          taskId: taskNumId,
          data: data,
        },
      });
}

export async function importDataFromLib(taskNumId) {
  return request('/api/task/importTarTestCasesFromLib', {
    method: 'POST',
    body: {
      taskId: taskNumId,
    },
  });
}

export async function exportDataToLib(taskNumId, data) {
  return request('/api/task/exportTarTestCasesToLib', {
    method: 'POST',
    body: {
      taskId: taskNumId,
      data: data,
    },
  });
}
