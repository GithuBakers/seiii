import React from 'react'
import TweenOne from 'rc-tween-one';

function ModelChildren(props) {
  return (
    <div>
      {props.modelValue}
      <TweenOne
        animation={{width:'100vw',duration:700,ease:"easeInExpo"}}
        style={{
          width:'0',
          height:'100vh',
          right:'0',
          top:'0',
          bottom:'0',
          zIndex:'-1',
          position:'absolute',
          background:'#111926'
        }}
      />
    </div>
  )
}

export default ModelChildren;
