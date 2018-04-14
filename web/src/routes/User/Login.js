import React, { Component } from 'react';
import { connect } from 'dva';
import { Link } from 'dva/router';
import { Checkbox, Alert } from 'antd';
import Login from 'components/Login';
import styles from './Login.less';

const { UserName, Password, Submit } = Login;

@connect(({ login, loading }) => ({
  login,
  submitting: loading.effects['login/login'],
}))
export default class LoginPage extends Component {


  handleSubmit = (err, values) => {
    if (!err) {
      this.props.dispatch({
        type: 'login/login',
        payload: {
          ...values,
        },
      });
    }
  };

  renderMessage = content => {
    return <Alert style={{ marginBottom: 24 }} message={content} type="error" showIcon />;
  };

  render() {
    const { login, submitting } = this.props;
    return (
      <div className={styles.main}>
        <Login onSubmit={this.handleSubmit}>
          <div>
            {login.status === 'error' &&
              !login.submitting &&
              this.renderMessage('账户或密码错误（admin/888888）')}
            <UserName name="user_name" placeholder="admin/initiator/worker" />
            <Password name="password" placeholder="888888/1/1" />
            <Submit style={{marginTop:'0'}} loading={submitting}>登录</Submit>
          </div>

          <div className={styles.other}>
            <Link className={styles.register} to="/user/register">
              注册账户
            </Link>
          </div>
        </Login>
      </div>
    );
  }
}
