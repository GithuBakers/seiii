import {Image} from "react-konva";
import React from 'react'

export default class MyImage extends React.Component{

  constructor(props){
    super(props);


    this.state={
      image:new window.Image()
    };
    this.state.image.src = this.props.url;

    this.state.image.onload = () => {
      if(!this.imageNode){
        return;
      }
      this.imageNode.getLayer().batchDraw();
    };
  }

  componentWillReceiveProps(nextProps){
    const image = new window.Image();
    image.src = nextProps.url;
    image.onload = () => {
      this.setState({
        image: image
      });
    };
  };


  render(){
    return(
      <Image image={this.state.image} ref={node => {
        this.imageNode = node;
      }}/>
    )
  }
}
