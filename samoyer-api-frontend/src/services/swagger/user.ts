// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** Create User This can only be done by the logged in User. POST /User */
export async function createUser(body: API.User, options?: { [key: string]: any }) {
  return request<any>('/user', {
    method: 'POST',
    data: body,
    ...(options || {}),
  });
}

/** Get User by User name GET /User/${param0} */
export async function getUserByName(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserByNameParams,
  options?: { [key: string]: any },
) {
  const { username: param0, ...queryParams } = params;
  return request<API.User>(`/user/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** Updated User This can only be done by the logged in User. PUT /User/${param0} */
export async function updateUser(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.updateUserParams,
  body: API.User,
  options?: { [key: string]: any },
) {
  const { username: param0, ...queryParams } = params;
  return request<any>(`/user/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  });
}

/** Delete User This can only be done by the logged in User. DELETE /User/${param0} */
export async function deleteUser(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteUserParams,
  options?: { [key: string]: any },
) {
  const { username: param0, ...queryParams } = params;
  return request<any>(`/user/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** Creates list of users with given input array POST /User/createWithArray */
export async function createUsersWithArrayInput(
  body: API.User[],
  options?: { [key: string]: any },
) {
  return request<any>('/user/createWithArray', {
    method: 'POST',
    data: body,
    ...(options || {}),
  });
}

/** Creates list of users with given input array POST /User/createWithList */
export async function createUsersWithListInput(body: API.User[], options?: { [key: string]: any }) {
  return request<any>('/user/createWithList', {
    method: 'POST',
    data: body,
    ...(options || {}),
  });
}

/** Logs User into the system GET /User/login */
export async function loginUser(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.loginUserParams,
  options?: { [key: string]: any },
) {
  return request<string>('/user/login', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** Logs out current logged in User session GET /User/logout */
export async function logoutUser(options?: { [key: string]: any }) {
  return request<any>('/user/logout', {
    method: 'GET',
    ...(options || {}),
  });
}
