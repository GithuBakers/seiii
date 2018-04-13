import React from 'react'
import Styles from './styles.css'

export default function(props){
  return <div style={props.isDark?{background:"#333"}:{}} className={Styles["sk-rotating-plane"]}/>
}
