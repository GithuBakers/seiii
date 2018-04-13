import ReactModal from 'react-modal';
import React from 'react'
import styles from './styles.css'
import ModelChildren from "./ModelChildren";

ReactModal.setAppElement('#root');
class BlackModel extends React.Component {
  render() {
    console.log(this.props.showModal);
    return (
      <ReactModal
        style={{
          overlay: {
            backgroundColor: this.props.background,
          }
        }}
        onRequestClose={this.props.onRequestClose}
      isOpen={this.props.showModal}
      className={styles.Modal}
      overlayClassName={styles.Overlay}
    >
        <ModelChildren modelValue={this.props.children}/>
    </ReactModal>
    )
  }
}


export default BlackModel;
