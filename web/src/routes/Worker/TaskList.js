import React, {PureComponent} from 'react';
import {connect} from 'dva';
import {Card, DatePicker, Form, Input, List, Select,} from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import styles from './style.less';

const FormItem = Form.Item;
const { Option } = Select;
const { RangePicker } = DatePicker;
const { TextArea } = Input;

@connect(({taskMarket, loading}) => ({
  taskMarket,
  loading: loading.effects['taskMarket/submitRegularForm'],
}))
export default class BasicForms extends PureComponent {
  handleSubmit = e => {
    e.preventDefault();
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        this.props.dispatch({
          type: 'taskMarket/fetchTaskList',
        });
      }
    });
  };
  render() {

    const {taskList, selectedTask} = this.props.taskMarket;
    const cardList = taskList ? (
      <List
        rowKey="id"
        loading={loading}
        grid={{ gutter: 24, xl: 4, lg: 3, md: 3, sm: 2, xs: 1 }}
        dataSource={taskList}
        renderItem={item => (
          <List.Item>
            <Card
              className={styles.card}
              hoverable
              cover={<img alt={item.cover} src={item.cover}/>}
            >
              <Card.Meta
                title={<a href="#">{item.task_name}</a>}/>
            </Card>
          </List.Item>
        )}
      />
    ) : null;

    return (
      <PageHeaderLayout
        title="基础表单"
        content="表单页用于向用户收集或验证信息，基础表单常见于数据项较少的表单场景。"
      >
        <cardList />
      </PageHeaderLayout>
    );
  }
}
