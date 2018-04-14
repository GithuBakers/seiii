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
  message,
} from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import AvatarUpload from '../../components/AvatarUpload';
import styles from './style.less';
import { routerRedux } from 'dva/router';
import { randomString } from '../../utils/random';
import Password from '../../components/PasswordSetting/Password';


const FormItem = Form.Item;
const { Option } = Select;
const { RangePicker } = DatePicker;
const { TextArea } = Input;


const tabList = [{
  key: 'normal',
  tab: '个人信息',
}, {
  key: 'password',
  tab: '密码',
}];

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
  loading: loading.effects['user/fetchCurrent'],
  normalLoading: loading.effects['user/updateWorkerProfile'],
}))

@Form.create()
export default class Setting extends PureComponent {

  state = {
    key: 'normal',
  };

  avatarUrl = undefined;

  onNormalSubmit = e => {
    e.preventDefault();
    this.props.form.validateFieldsAndScroll(async (err, values) => {
      if (!err) {
        const result = values;
        result.avatar = this.avatarUrl;
        await this.props.dispatch({
          type: 'user/updateWorkerProfile',
          payload: result,
        });
        message.success('设置成功~')
      }
    });

  };

  render() {
    const { submitting } = this.props;
    const { getFieldDecorator, getFieldValue } = this.props.form;

    const { nick_name, avatar } = this.props.user.currentUser;
    this.avatarUrl = avatar;
    const normalSetting = (
      <div>
        <Form onSubmit={this.onNormalSubmit} hideRequiredMark style={{ marginTop: 8 }}>

          <FormItem  {...formItemLayout} label="新昵称">
            {getFieldDecorator('nick_name', {
              rules: [
                {
                  required: true,
                  message: '请输入昵称！',
                },
              ],
              initialValue: nick_name,
            })(<Input size="large" onKeyUp={e => console.log(e)} placeholder="昵称"/>)}
          </FormItem>
          <FormItem  {...formItemLayout} label="新头像">
            {getFieldDecorator('avatar')(<AvatarUpload defaultUrl={avatar} setAvatar={url => this.avatarUrl = url}/>)}
          </FormItem>
          <FormItem {...submitFormLayout} style={{ marginTop: 32 }}>
            <Button
              style={{ width: '100%' }}
              type="primary"
              htmlType="submit"
              loading={this.props.normalLoading}
            >
              提交
            </Button>
          </FormItem>
        </Form>

      </div>
    );


    const contentList = {
      normal: normalSetting,
      password: <Password />,
    };


    return (
      <PageHeaderLayout
        title="个人信息设置"
        content="普通设置可以事实更新，次数不限。密码修改需要重新登录"
      >
        <Card
          loading={this.props.loading}
          bordered={false}
          tabList={tabList}
          onTabChange={(key) => this.setState({ 'key': key })}
        >
          {contentList[this.state.key]}
        </Card>
      </PageHeaderLayout>
    );
  }
}
