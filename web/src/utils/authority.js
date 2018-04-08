// use localStorage to store the authority info, which might be sent from server in actual project.
export function getAuthority() {
  return localStorage.getItem('antd-pro-authority');
}

export function setAuthority(authority) {
  if (Array.isArray(authority)) {
    return localStorage.setItem('antd-pro-authority', authority.join());
  }
  return localStorage.setItem('antd-pro-authority', authority);
}
