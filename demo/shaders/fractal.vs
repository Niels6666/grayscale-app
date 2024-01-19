#version 460 core


layout(location = 0) in vec2 position;

uniform mat4 transform;
uniform vec2 render_size;
uniform vec2 translation;
uniform float scale;
out vec2 spaceCoords;

void main(){
	gl_Position = transform * vec4(position, 0.0, 1.0);
	spaceCoords = vec2(position)*render_size*scale+translation;
}