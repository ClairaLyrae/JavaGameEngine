#version 150
#define MAX_LIGHTS 10

uniform mat4 m;
uniform mat4 v;
uniform mat4 mv;
uniform mat4 p;

uniform vec3 light_position[MAX_LIGHTS];
uniform vec3 light_direction[MAX_LIGHTS];
uniform vec4 light_color_diffuse[MAX_LIGHTS];
uniform vec4 light_color_specular[MAX_LIGHTS];
uniform vec4 light_color_ambient[MAX_LIGHTS];
uniform float light_spot_angle[MAX_LIGHTS];
uniform float light_spot_exponent[MAX_LIGHTS];
uniform float light_limit[MAX_LIGHTS];
uniform int light_valid[MAX_LIGHTS];

in vec3 in_position;
in vec3 in_normal;
in vec4 in_tangent;
in vec2 in_texcoord;
in vec4 in_color;

out vec3 light_direction_world[MAX_LIGHTS];

out vec3 position;
out vec3 normal;
out vec3 tangent;
out vec3 bitangent;
out vec2 texcoord;
out vec4 color;

void main(void) 
{
	mat3 normalmatrix = transpose( inverse( mat3( transpose( mv ))));
	
	vec4 mv_position = vec4(in_position, 1) * mv;
	
	normal = normalize(normalmatrix * in_normal);
	tangent = normalize(normalmatrix * in_tangent.xyz);
	bitangent = normalize(cross(normal, tangent)) * in_tangent.w;
	position = mv_position.xyz;
	texcoord = in_texcoord;
	
	for(int i = 0; i < MAX_LIGHTS; i++)
	{
    	if(light_valid[i] == 1)
    	{
    		light_direction_world[i] = normalize((vec4(light_position[i], 1)*v).xyz - position);
    	}
	}
	
	gl_Position = mv_position * p;
}