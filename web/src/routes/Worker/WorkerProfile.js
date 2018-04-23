import React, { PureComponent } from 'react';
import { connect } from 'dva';
import {
  Form,
  Input,
  DatePicker,
  Select,
  Button,
  Card,
  List,
  Divider,
  Tag,
} from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import styles from './WorkerProfile.less';
import DescriptionList from '../../components/DescriptionList/DescriptionList';
import Description from '../../components/DescriptionList/Description';
import { routerRedux } from 'dva/router';
import DetailCard from '../../components/DetailCard';
import Ellipsis from '../../components/Ellipsis';

const FormItem = Form.Item;
const { Option } = Select;
const { RangePicker } = DatePicker;
const { TextArea } = Input;

@connect(({ user, loading }) => ({
  user,
  loading: loading.effects['user/fetchCurrent'],
}))
export default class WorkerProfile extends PureComponent {

  state = {
    selectedCriterion:{},
    modalVisible: false,
  };

  componentDidMount() {
    this.props.dispatch({
      type:'user/fetchCurrent',
    })
  }

  render() {

    const { user_name, role, avatar, nick_name, credit, rank, dependencies } = this.props.user.currentUser;
    const {selectedCriterion,modalVisible}=this.state;

    const showDetail = async criterionItem => {
      await this.setState({ modalVisible: true ,selectedCriterion: criterionItem });
    };

    const DetailModal= props => (
      <DetailCard
        visible={modalVisible}
        onCancel={() => this.setState({ modalVisible: false })}
        title={selectedCriterion.criterion_name}
        cover={selectedCriterion.cover}
        content={[
          { title: '标准集详细要求', value: selectedCriterion.requirement },
          { title: '任务类型', value: selectedCriterion.type },
          { title: '工人通过标准', value: `${selectedCriterion.aim} 张/人` },
          { title: '关键词', value: selectedCriterion.keywords?selectedCriterion.keywords.map(e => <Tag>{e}</Tag>):<div>无关键词</div> },
        ]}
        footer={null}
      />

    );
    const avatarContainer = (
      <div style={{
        marginTop: '-60px',
      }}
      >
        <div style={{
          float: 'right',
          width: '100px',
          height: '100px',
          background: 'rgba(128, 128, 128, 0.45)',
          borderRadius: '10px',
          overflow: 'hidden',
        }}
        >
          {avatar?<img width={100} alt="example" src={avatar} />:<div />}
        </div>
      </div>
    );

    const content = (
      <div>
        <p>"您可以在这里查看自己的个人信息。"</p>
        <div>
          <Button
            onClick={()=>{
            this.props.dispatch(
              routerRedux.push({
                pathname: '/worker/setting',
              })
            );
          }}
            icon="setting"
          >EDIT
          </Button>
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
        <DetailModal />
        <Card loading={this.props.loading} style={{ marginBottom: 24 }} bordered={false}>
          <DescriptionList size="large" title="账户信息">
            <Description term="昵称">{nick_name}</Description>
            <Description term="用户名">{user_name}</Description>
          </DescriptionList>
          <Divider style={{ marginBottom: 32 }} />
          <DescriptionList size="large" title="身份信息">

            <Description term="身份">{role}</Description>
            <Description term="积分">{credit}</Description>
            <Description term="排名">{rank}</Description>
          </DescriptionList>
        </Card>
        <Card
          bordered={false}
          title="已完成的标准集"
          style={{ marginTop: 24 }}
          bodyStyle={{ padding: '0 32px 40px 32px' }}
        >
          <List
            size="large"
            rowKey="id"
            itemLayout="vertical"
            loading={this.props.loading}
            dataSource={dependencies}
            renderItem={item => (
              <List.Item
                className={styles.list}
                style={{ cursor: 'pointer' }}
                onClick={() =>showDetail(item)}
                extra={<img width={272} alt="logo" src={item.cover} />}
                actions={[<Tag color="blue">{item.type}</Tag>]}
              >
                <List.Item.Meta
                  title={<a href={item.href}>{item.criterion_name}</a>}
                  description={<div style={{ maxWidth: 600 }}><Ellipsis lines={3}>{item.requirement}</Ellipsis></div>}
                />

              </List.Item>

            )}
          />
        </Card>
      </PageHeaderLayout>
    );
  }
}
