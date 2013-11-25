#version 150

uniform vec4 light_position;
uniform vec4 light_diffuse;
uniform vec4 light_ambient;

uniform sampler2D tex_diffuse;
uniform sampler2D tex_normal;
uniform sampler2D tex_specular;
uniform sampler2D tex_emissive;

in vec3 l_dir;
in vec3 view_dir;

in vec3 position;
in vec3 normal;
in vec3 tangent;
in vec3 bitangent;
in vec2 texcoord;
in vec4 color;

out vec4 fragColor;

void main(void) 
{
    vec3 N = normal;
    
    vec3 nmap = (texture2D(tex_normal, texcoord).rgb)*2.0;
    N = normalize((tangent * nmap.r) + (bitangent * nmap.g) + (normal * nmap.b));
    
    float brightness = max(0.0, dot(N, l_dir));
	float spec = 2*pow(max(0.0, dot(N, normalize(reflect(-l_dir, N)))), 50);
	
    vec4 emissive = texture2D(tex_emissive, texcoord);
	vec4 diffuse = texture2D(tex_diffuse, texcoord)*brightness;
	vec4 specular = texture2D(tex_specular, texcoord)*spec;
    //vec4 emissive = vec4(0, 0, 0, 1);
	//vec4 diffuse = vec4(0.5, 0.5, 0.5, 1)*brightness;
	//vec4 specular = vec4(0.9, 0.9, 0.9, 1)*spec;
	//fragColor.rgb = tangent;
    fragColor = diffuse + emissive + specular;
}