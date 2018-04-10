// use localStorage to store the authority info, which might be sent from server in actual project.
export function getAuthority() {
  return localStorage.getItem('current-authority');
}

export function setAuthority(authority) {
  if (Array.isArray(authority)) {
    return localStorage.setItem('current-authority', authority.join());
  }
  return localStorage.setItem('current-authority', authority);
}

export function setToken(token) {
  return localStorage.setItem('current-token', token);
}

export function getToken() {
  return localStorage.getItem('current-token');
}

export function setUserName(userName) {
  return localStorage.setItem('current-user', userName);
}

export function getUserName() {
  return localStorage.getItem('current-user');
}
