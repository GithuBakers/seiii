import React, { PureComponent } from 'react';
import { connect } from 'dva';
import {
  Form,
  Input,
  DatePicker,
  Select,
  Button,
  Card,
  InputNumber,
  Radio,
  Icon,
  Tooltip,
  Popover,
  Progress,
} from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import AvatarUpload from '../AvatarUpload/index';
import styles from '../../routes/Worker/style.less';
import { routerRedux } from 'dva/router';
import { randomString } from '../../utils/random';

const FormItem = Form.Item;
const { Option } = Select;
const { RangePicker } = DatePicker;
const { TextArea } = Input;

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

const formItemLayout = {
  labelCol: {
    xs: { span: 24 },
    sm: { span: 7 },
  },
  wrapperCol: {
    xs: { span: 24 },
    sm: { span: 12 },
    md: { span: 10 },
  },
};

const submitFormLayout = {
  wrapperCol: {
    xs: { span: 24, offset: 0 },
    sm: { span: 10, offset: 7 },
  },
};

@connect(({ user, loading }) => ({
  user,
  passwordLoading: loading.effects['user/updatePassword'],
}))

@Form.create()
export default class Password extends PureComponent {
  state = {
    confirmDirty: false,
    visible: false,
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

  onPasswordSubmit = e => {
    e.preventDefault();
    this.props.form.validateFieldsAndScroll(async (err, values) => {
      if (!err) {
        const result = values;
        console.log('form', values);
        result.avatar = this.avatarUrl;
        await this.props.dispatch({
          type: 'user/updatePassword',
          payload: result,
        });
      }
    });

  };

  render() {
    const { submitting } = this.props;
    const { getFieldDecorator, getFieldValue } = this.props.form;


    const passWordSetting = (
      <div>
        <FormItem {...formItemLayout} label="旧密码">
          {getFieldDecorator('ori_password', {
            rules: [
              {
                required: true,
                message: '请确认密码！',
              },
            ],
          })(<Input size="large" type="password" placeholder="请先输入旧密码"/>)}
        </FormItem>
        <FormItem {...formItemLayout} label="新密码">
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
            })(<Input size="large" type="password" placeholder="至少6位密码，区分大小写"/>)}
          </Popover>
        </FormItem>
        <FormItem {...formItemLayout} label="确认密码">
          {getFieldDecorator('new_password', {
            rules: [
              {
                required: true,
                message: '请确认密码！',
              },
              {
                validator: this.checkConfirm,
              },
            ],
          })(<Input size="large" type="password" placeholder="再次输入新密码"/>)}
        </FormItem>
        <FormItem {...submitFormLayout} style={{ marginTop: 32 }}>
          <Button
            style={{ width: '100%' }}
            type="primary"
            htmlType="submit"
            loading={this.props.passwordLoading}
          >
            提交
          </Button>
        </FormItem>
      </div>

    );


    return (
      <Form onSubmit={this.onPasswordSubmit} hideRequiredMark style={{ marginTop: 8 }}>
        {passWordSetting}
      </Form>
    );
  }
}
