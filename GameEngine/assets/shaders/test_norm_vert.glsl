#version 330

uniform mat4 mv;
uniform mat4 p;

in vec3 in_position;
in vec3 in_normal;
in vec4 in_tangent;

out vec3 normal;
out vec3 tangent;
out vec3 bitangent;

void main(void) 
{
	normal = normalize(in_normal);
	tangent = normalize(in_tangent.xyz);
	bitangent = normalize(cross(in_normal, tangent) * in_tangent.w);
	
	gl_Position = vec4(in_position, 1);
}