import React from 'react';
// import R from 'ramda';
import Konva from 'konva';
import { Layer, Rect, Stage, Group, Transformer} from 'react-konva';

export default class ColoredRect extends React.Component {
  constructor(props) {
    super(props);
  }


  render() {
    return (
      <Group>
        {this.props.select
          ?<Handler
          id={this.props.id}
        />:null}
        <Rect
          name={this.props.id}
          x={this.props.x}
          y={this.props.y}
          width={this.props.width}
          height={this.props.height}
          fill={this.props.color?this.props.color.fill:'#ffffff24'}
          stroke={this.props.color?this.props.color.stroke:"white"}
          onMouseLeave={(e)=>this.props.onMouseLeave(e)}
          onMouseEnter={(e)=>this.props.onMouseEnter(e)}
          onDragStart={(e)=>this.props.onDragStart(e)}
          // onDragMove={(e)=>this.props.onDragMove(e)}
          onDragEnd={(e)=>this.props.onDragEnd(e)}
          onClick={(e)=>{
            // console.log(e);
            this.props.onClick(e)
          }}
          onTransform={(e) => {
            if (this.props.select) {
              this.props.onTransform(e.currentTarget.attrs);
            }
          }}
          draggable
        />
      </Group>
    );
  }
}

class Handler extends React.Component {
  componentDidMount() {
    // not really "react-way". But it works.
    const stage = this.transformer.getStage();
    const rectangle = stage.findOne("."+this.props.id);
    this.transformer.attachTo(rectangle);
    this.transformer.getLayer().batchDraw();
  }
  render() {
    return (
      <Transformer
        ref={node => {
          this.transformer = node;
        }}
      />
    );
  }
}

// class App extends React.Component {
//   constructor(props) {
//     super(props);
//     this.state = {
//       shapes: [],           // list of dimensions to be rendered as shapes
//       isDrawing: false,     // in the process of drawing a shape
//       isDrawingMode: true,  // allow shapes to be drawn
//     }
//   }
//
//   handleClick = (e) => {
//     if (!this.state.isDrawingMode) return;
//     // if we are drawing a shape, a click finishes the drawing
//     if(this.state.isDrawing) {
//       this.setState({
//         isDrawing: !this.state.isDrawing,
//       })
//       return;
//     }
//
//     // otherwise, add a new rectangle at the mouse position with 0 width and height,
//     // and set isDrawing to true
//     const newShapes = this.state.shapes.slice();
//     newShapes.push({
//       x: e.evt.layerX,
//       y: e.evt.layerY,
//       width: 0,
//       height: 0,
//     });
//
//     this.setState({
//       isDrawing: true,
//       shapes: newShapes,
//     })
//   }
//
//   handleMouseMove= (e) => {
//     if (!this.state.isDrawingMode) return;
//
//     const mouseX = e.evt.layerX;
//     const mouseY = e.evt.layerY;
//
//     // update the current rectangle's width and height based on the mouse position
//     if (this.state.isDrawing) {
//       // get the current shape (the last shape in this.state.shapes)
//       const currShapeIndex = this.state.shapes.length - 1;
//       const currShape = this.state.shapes[currShapeIndex];
//       const newWidth = mouseX - currShape.x;
//       const newHeight = mouseY - currShape.y;
//
//       const newShapesList = this.state.shapes.slice();
//       newShapesList[currShapeIndex] = {
//         x: currShape.x,   // keep starting position the same
//         y: currShape.y,
//         width: newWidth,  // new width and height
//         height: newHeight
//       }
//
//       this.setState({
//         shapes: newShapesList,
//       });
//     }
//   }
//
//   handleCheckboxChange = () => {
//     // toggle drawing mode
//     this.setState({
//       isDrawingMode: !this.state.isDrawingMode,
//     })
//   }
//
//   render() {
//     return (
//       <div>
//        <Stage width={window.innerWidth} height={window.innerHeight}
//                onContentClick={this.handleClick}
//                onContentMouseMove={this.handleMouseMove}
//         >
//           <Layer ref='layer'>
//             {/*
//               render the shapes array - each element in 'shapes' renders a ColoredRect component
//               with that element's dimensions. Any time these dimensions change (in the handle
//               functions), the ColoredRect rerenders to reflect those changes.
//             */}
//             {this.state.shapes.map(shape => {
//               return (
//                 <ColoredRect
//                   x={shape.x}
//                   y={shape.y}
//                   width={shape.width}
//                   height={shape.height}
//                   isDrawingMode={this.state.isDrawingMode}
//                   // pass isDrawingMode so we know when we can click on a shape
//                 />
//               );
//             })}
//           </Layer>
//         </Stage>
//       </div>
//     );
//   }
// }

