import React, {PureComponent} from 'react';
import {connect} from 'dva';
import { Tag, Card, message, List } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import styles from './AllCriterion.less';
import { receiveWorkerTask } from '../../services/apiList'
import DetailCard from '../../components/DetailCard/DetailCard';


@connect(({ initiatorCriterion, loading }) => ({
  initiatorCriterion,
  loading: loading.effects['initiatorCriterion/fetchAllCriterion'],
}))
export default class TaskList extends PureComponent {

  state={
    modalVisible:false,
    selectedCriterion: {},
  };

  async componentDidMount() {
    await this.props.dispatch({
      type: 'initiatorCriterion/fetchAllCriterion',
    });
  }


  handleCardClicked = async criterionItem => {
    await this.setState({ modalVisible: true, selectedCriterion: criterionItem });
  };

  render() {

    const { allCriterion } = this.props.initiatorCriterion;
    const { modalVisible, selectedCriterion } = this.state;
    console.log("allCriterion",allCriterion);
    const CardList = () => allCriterion ? (
      <List
        rowKey="id"
        loading={this.props.loading}
        grid={{ gutter: 24, xl: 4, lg: 3, md: 3, sm: 2, xs: 1 }}
        dataSource={allCriterion}
        className={styles.coverCardList}
        renderItem={item => (
          <List.Item>
            <Card
              className={styles.card}
              hoverable
              cover={<img style={{height:'50%'}} alt={item.cover} src={item.cover} />}
              onClick={() => this.handleCardClicked(item)}
            >
              <Card.Meta
                title={<a>{item.criterion_name}</a>}
                description={item.requirement}
              />
              <div className={styles.cardItemContent}>
                <div className={styles.tag}>
                  <Tag color="blue">{item.type}</Tag>
                </div>
              </div>
            </Card>
          </List.Item>
        )}
      />
    ) : null;

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

    return (
      <PageHeaderLayout
        title="公开的标准集"
        content="您可以随意浏览开放的标准集，并在创建项目的时候使用它们，这会显著提高众包工人的工作质量，但同时提高工作门槛"
      >
        <DetailModal/>
        <div className={styles.cardList}><CardList /></div>

      </PageHeaderLayout>
    );
  }
}
