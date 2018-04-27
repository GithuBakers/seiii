import React, { Component } from 'react';
import { connect } from 'dva';
import { routerRedux, Link } from 'dva/router';
import { Radio, Form, Input, Button, Select, Row, Col, Popover, Progress,notification,DatePicker } from 'antd';
import styles from './Register.less';
import AvatarUpload from '../../components/AvatarUpload';

const RadioGroup = Radio.Group;
const FormItem = Form.Item;
const { Option } = Select;
const InputGroup = Input.Group;

const passwordStatusMap = {
  ok: <div className={styles.success}>强度：强</div>,
  pass: <div className={styles.warning}>强度：中</div>,
  poor: <div className={styles.error}>强度：太短</div>,
};

const passwordProgressMap = {
  ok: 'success',
  pass: 'normal',
  poor: 'exception',
};

@connect(({ register, loading }) => ({
  register,
  submitting: loading.effects['register/submit'],
}))
@Form.create()
export default class Register extends Component {
  state = {
    confirmDirty: false,
    visible: false,
    help: '',
    role: 'WORKER',
    avatarUrl: undefined,
  };

  componentWillReceiveProps(nextProps) {
    const account = this.props.form.getFieldValue('nick_name');
    if (nextProps.register.status === 'ok') {
      this.props.dispatch(
        routerRedux.push({
          pathname: '/user/register-result',
          state: {
            account,
          },
        })
      );
      this.props.dispatch({
        type: 'register/registerHandle',
        payload: {
          status: undefined,
        },
      });
    } else if (nextProps.register.status === 'error') {
      notification.warning({
        message: '用户名已被注册',
        description: '请再尝试使用一个新的用户名进行注册.',
      });
    }
  }

  componentWillUnmount() {
    clearInterval(this.interval);
  }

  getPasswordStatus = () => {
    const { form } = this.props;
    const value = form.getFieldValue('password');
    if (value && value.length > 9) {
      return 'ok';
    }
    if (value && value.length > 5) {
      return 'pass';
    }
    return 'poor';
  };

  handleSubmit = e => {
    e.preventDefault();
    this.props.form.validateFields({ force: true }, (err, values) => {
      if (!err) {
        this.props.dispatch({
          type: 'register/submit',
          payload: {
            ...values,
            birthday:values['birthday'].format('YYYY-MM-DD'),
            avatar: this.state.avatarUrl,
          },
        });

      }
    });
  };

  checkConfirm = (rule, value, callback) => {
    const { form } = this.props;
    if (value && value !== form.getFieldValue('password')) {
      callback('两次输入的密码不匹配!');
    } else {
      callback();
    }
  };

  checkPassword = (rule, value, callback) => {
    if (!value) {
      this.setState({
        help: '请输入密码！',
        visible: !!value,
      });
      callback('error');
    } else {
      this.setState({
        help: '',
      });
      if (!this.state.visible) {
        this.setState({
          visible: !!value,
        });
      }
      if (value.length < 6) {
        callback('error');
      } else {
        const { form } = this.props;
        if (value && this.state.confirmDirty) {
          form.validateFields(['confirm'], { force: true });
        }
        callback();
      }
    }
  };

  onRoleChange = e => {
    console.log(e);
    this.setState({ role: e.target.value });
  };

  renderPasswordProgress = () => {
    const { form } = this.props;
    const value = form.getFieldValue('password');
    const passwordStatus = this.getPasswordStatus();
    return value && value.length ? (
      <div className={styles[`progress-${passwordStatus}`]}>
        <Progress
          status={passwordProgressMap[passwordStatus]}
          className={styles.progress}
          strokeWidth={6}
          percent={value.length * 10 > 100 ? 100 : value.length * 10}
          showInfo={false}
        />
      </div>
    ) : null;
  };

  render() {
    const { form, submitting } = this.props;
    const { getFieldDecorator } = form;
    return (
      <div className={styles.main}>
        <h3>注册</h3>
        <Form onSubmit={this.handleSubmit}>
          <Row gutter={20}>
            <Col span={16}>
              <FormItem>
                {getFieldDecorator('nick_name', {
                  rules: [
                    {
                      required: true,
                      message: '请输入昵称！',
                    },
                  ],
                })(<Input size="large" placeholder="昵称" />)}
              </FormItem>
              <FormItem>
                {getFieldDecorator('user_name', {
                  rules: [
                    {
                      required: true,
                      message: '请输入账户名！',
                    },
                  ],
                })(<Input size="large" placeholder="账户名" />)}
              </FormItem>
            </Col>
            <Col span={8}>
              <AvatarUpload setAvatar={url => this.setState({ avatarUrl: url })} />
            </Col>
          </Row>
          <FormItem help={this.state.help}>
            <Popover
              content={
                <div style={{ padding: '4px 0' }}>
                  {passwordStatusMap[this.getPasswordStatus()]}
                  {this.renderPasswordProgress()}
                  <div style={{ marginTop: 10 }}>
                    请至少输入 6 个字符。请不要使用容易被猜到的密码。
                  </div>
                </div>
              }
              overlayStyle={{ width: 240 }}
              placement="right"
              visible={this.state.visible}
            >
              {getFieldDecorator('password', {
                rules: [
                  {
                    validator: this.checkPassword,
                  },
                ],
              })(<Input size="large" type="password" placeholder="至少6位密码，区分大小写" />)}
            </Popover>
          </FormItem>
          <FormItem>
            {getFieldDecorator('confirm', {
              rules: [
                {
                  required: true,
                  message: '请确认密码！',
                },
                {
                  validator: this.checkConfirm,
                },
              ],
            })(<Input size="large" type="password" placeholder="确认密码" />)}
          </FormItem>
          <FormItem>
            {getFieldDecorator('sex', {
              initialValue: 'NA',
            })(
              <RadioGroup>
                <Radio value="MALE">男性</Radio>
                <Radio value="FEMALE">女性</Radio>
                <Radio value="NA">不愿透露</Radio>
              </RadioGroup>
            )}
          </FormItem>
          <FormItem>
            {getFieldDecorator('birthday', {rules: [{ type: 'object', required: true, message: '抱歉，您的生日对我们很重要' }]})(
              <DatePicker size="large"  style={{width:'100%'}} placeholder='请输入生日' />
            )}
          </FormItem>
          <FormItem>
            {getFieldDecorator('role', {
              initialValue: 'WORKER',
            })(
              <RadioGroup>
                <Radio value="WORKER">众标工人</Radio>
                <Radio value="INITIATOR">众标发起者</Radio>
              </RadioGroup>
            )}
          </FormItem>
          <FormItem>
            <Button
              size="large"
              loading={submitting}
              className={styles.submit}
              type="primary"
              htmlType="submit"
            >
              注册
            </Button>
            <Link className={styles.login} to="/user/login">
              使用已有账户登录
            </Link>
          </FormItem>
        </Form>
      </div>
    );
  }
}
