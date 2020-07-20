import request from '../utils/request';

export async function getTestJson1() {
  return request('/api/getJsonDatas?fileId=1');
}

export async function getTestJson2() {
  return request('/api/getJsonDatas?fileId=2');
}

export async function getConfiguration() {
  return request('/api/getJsonDatas?fileId=1');
}

export async function getProjects() {
  return request('/api/project/getProjectAttrs');
}

export async function getResourceJson() {
  return request('/api/getJsonDatas?fileId=4');
}

export async function getCarframe() {
  return request('/api/getJsonDatas?fileId=5');
}

export async function getTask() {
  return request('/api/getJsonDatas?fileId=7');
}

export async function getFault() {
  return request('/api/fault/getFaultAttrs');
}

export async function getUsers() {
  return request('/api/getJsonDatas?fileId=9');
}

export async function getRole() {
  return request('/api/getJsonDatas?fileId=10');
}

export async function getSenior() {
  return request('/api/getJsonDatas?fileId=11');
}

export async function getTimerecord() {
  return request('/api/getJsonDatas?fileId=12');
}

export async function getDataAnalysis() {
  return request('/api/getJsonDatas?fileId=13');
}

export async function getDailyReport() {
  return request('/api/getJsonDatas?fileId=14');
}

export async function getMessage() {
  return request('/api/getJsonDatas?fileId=15');
}
