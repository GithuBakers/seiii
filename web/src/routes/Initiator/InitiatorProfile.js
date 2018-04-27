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
  Divider,
} from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import styles from './style.less';
import DescriptionList from '../../components/DescriptionList/DescriptionList';
import Description from '../../components/DescriptionList/Description';
import { routerRedux } from 'dva/router';

const FormItem = Form.Item;
const { Option } = Select;
const { RangePicker } = DatePicker;
const { TextArea } = Input;

@connect(({ user, loading }) => ({
  user,
  loading: loading.effects['user/fetchCurrent'],
}))
export default class WorkerProfile extends PureComponent {


  render() {

    const { user_name, role, avatar, nick_name,sex,birthday } = this.props.user.currentUser;

    
    const avatarContainer = (
      <div style={{
        marginTop: '-60px',
      }}>
        <div style={{
          float: 'right',
          width: '100px',
          height: '100px',
          background: 'rgba(128, 128, 128, 0.45)',
          borderRadius: '10px',
          overflow: 'hidden',
        }}>
          {avatar ? <img width={100} alt="example" src={avatar}/> : <div/>}
        </div>
      </div>
    );

    const content = (
      <div>
        <p>"您可以在这里查看自己的个人信息。"</p>
        <div>
          <Button onClick={() =>
            this.props.dispatch(
              routerRedux.push({
                pathname: '/initiator/setting',
              }),
            )} icon="setting">EDIT</Button>
        </div>
      </div>
    );


    return (
      <PageHeaderLayout
        loading={this.props.loading}
        title={nick_name ? `HELLO~ ${nick_name}` : `Let me think...`}
        content={content}
        extraContent={avatarContainer}
      >
        <Card loading={this.props.loading} style={{ marginBottom: 24 }} bordered={false}>
          <DescriptionList size="large" title="账户信息">
            <Description term="昵称">{nick_name}</Description>
            <Description term="用户名">{user_name}</Description>
            <Description term="性别">{sex}</Description>
            <Description term="生日">{birthday}</Description>
          </DescriptionList>
          <Divider style={{ marginBottom: 32 }}/>
          <DescriptionList size="large" title="身份信息">

            <Description term="身份">{role}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
