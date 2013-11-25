#version 150

uniform mat4 mv;
uniform mat4 p;

uniform vec4 light_position;
uniform vec4 light_diffuse;
uniform vec4 light_ambient;

in vec3 in_position;
in vec3 in_normal;
in vec4 in_tangent;
in vec2 in_texcoord;
in vec4 in_color;

out vec3 l_dir;

out vec3 position;
out vec3 normal;
out vec3 tangent;
out vec3 bitangent;
out vec2 texcoord;
out vec4 color;

void main(void) 
{
	mat3 normalmatrix = transpose( inverse( mat3( transpose( mv ))));
	
	normal = normalize(normalmatrix * in_normal);
	tangent = normalize(normalmatrix * in_tangent.xyz);
	bitangent = normalize(cross(normal, tangent)) * in_tangent.w;
	
	position = (vec4(in_position, 1.0) * mv).xyz;
	texcoord = in_texcoord;
	
	gl_Position = vec4(in_position, 1) * mv * p;
	
    l_dir = normalize((light_position).xyz - position);
}