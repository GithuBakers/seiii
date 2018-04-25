import React, { PureComponent } from 'react';
import {Modal,Row,Col} from 'antd'


const ListInfo = ({ title, value }) => (
  <div style={{ padding: '20px 0' }}>
    <Row>
      <Col sm={10} xs={24}>
        <div style={{ fontWeight: 'bold' }}>{title}</div>
      </Col>
      <Col sm={14} xs={24}>
        <div>{value}</div>
      </Col>
    </Row>
  </div>
);

export default class DetailCard extends PureComponent {
  render() {
    return (
      <Modal
        width={600}
        visible={this.props.visible}
        destroyOnClose="true"
        onCancel={this.props.onCancel}
        footer={this.props.footer}
      >
        <div style={{ margin: '-24px' }}>
          <img style={{ width: '100%', margin: '0 auto' }} src={this.props.cover} />
          <div style={{ maxWidth: '500px', margin: '40px auto 10px', paddingBottom: '20px' }}>
            <h1 style={{ textAlign: 'center' }}>{this.props.title}</h1>
            {this.props.content.map((e,index)=>(<ListInfo key={index} title={e.title} value={e.value} />))}
          </div>
        </div>
      </Modal>);
  }
}

