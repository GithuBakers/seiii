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
import DescriptionList from '../../components/DescriptionList/DescriptionList';
import Description from '../../components/DescriptionList/Description';
import { routerRedux } from 'dva/router';

const FormItem = Form.Item;
const { Option } = Select;
const { RangePicker } = DatePicker;
const { TextArea } = Input;

@connect(({ system, loading }) => ({
  system,
  loading: loading.effects['system/fetchSystemInformation'],
}))
export default class WorkerProfile extends PureComponent {


  componentDidMount(){
    this.refreshData();
  }

  refreshData=()=>{
    this.props.dispatch({ type: 'system/fetchSystemInformation'});
  };

  render() {

    const {
      initiator_number,
      worker_number,
      total_user_number,
      unfinished_number,
      finished_number,
      total_task_number } = this.props.system;

    const content = (
      <div>
        <p>"这里放置最新的最有用的系统信息，您可以随时刷新查看最新情景"</p>
        <div>
          <Button onClick={()=>this.refreshData()} loading={this.props.loading} icon="reload">RELOAD</Button>
        </div>
      </div>
    );
    return (
      <PageHeaderLayout
        loading={this.props.loading}
        title='系统信息一览'
        content={content}
      >
        <Card loading={this.props.loading} style={{ marginBottom: 24 }} bordered={false}>
          <DescriptionList size="large" title="用户信息">
            <Description term="发起者用户数">{initiator_number}</Description>
            <Description term="工人用户数">{worker_number}</Description>
            <Description term="总用户数">{total_user_number}</Description>

          </DescriptionList>
          <Divider style={{ marginBottom: 32 }} />
          <DescriptionList size="large" title="任务信息">
            <Description term="未结束任务">{unfinished_number}</Description>
            <Description term="已结束任务">{finished_number}</Description>
            <Description term="总任务数">{total_task_number}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
