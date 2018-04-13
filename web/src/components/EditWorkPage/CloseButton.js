import button from './button.css'
import {connect} from "dva";
import React from 'react';

function CloseButton({dispatch}) {
  return (
    <div
      className={button.button}
      onClick={() => {
        dispatch({type: 'editWorkModel/setOpenState', payload: {isOpen: false}});
      }}
    >
      X
    </div>
  )
}


export default connect()(CloseButton)

