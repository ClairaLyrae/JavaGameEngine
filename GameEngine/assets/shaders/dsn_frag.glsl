#version 150
#define MAX_LIGHTS 10

uniform mat4 v;

uniform sampler2D tex_diffuse;
uniform sampler2D tex_normal;
uniform sampler2D tex_specular;

uniform vec3 light_position[MAX_LIGHTS];
uniform vec4 light_color_diffuse[MAX_LIGHTS];
uniform vec4 light_color_specular[MAX_LIGHTS];
uniform vec4 light_color_ambient[MAX_LIGHTS];
uniform float light_spot_angle[MAX_LIGHTS];
uniform float light_spot_exponent[MAX_LIGHTS];
uniform float light_limit[MAX_LIGHTS];
uniform float light_intensity[MAX_LIGHTS];
uniform int light_valid[MAX_LIGHTS];

in vec3 position;
in vec3 normal;
in vec3 tangent;
in vec3 bitangent;
in vec2 texcoord;
in vec4 color;
in vec3 light_direction_world[MAX_LIGHTS];

out vec4 fragColor;

void main(void) 
{
    vec3 N = normal;
    
    vec3 nmap = ((texture2D(tex_normal, texcoord).rgb)*2.0)-1;
    N = normalize((tangent * nmap.r) + (bitangent * nmap.g) + (normal * nmap.b));
        
    vec4 diffuse;
    vec4 specular;
    float atten;
    vec3 clpos;
    float dist;
    float range;
    
    for(int i = 0; i < MAX_LIGHTS; i++)
    {
    	clpos = (vec4(light_position[i], 1) * v).xyz;
    	if(light_valid[i] == 1)
    	{
    		atten = 1;
    		range = light_limit[i];
    		if(range > 0.0)
    		{
    			dist = distance(clpos, position);
    			if(dist > range)
    				atten = 0;
    			else
    				atten = (range - dist)/range;
    		}
    		diffuse += light_color_diffuse[i]*max(0.0, dot(N, light_direction_world[i]))*atten*light_intensity[i] + light_color_ambient[i];
			specular += light_color_specular[i]*2*pow(max(0.0, dot(N, normalize(reflect(-light_direction_world[i], N)))), 50)*atten;
		}
	}
    
    diffuse *= texture2D(tex_diffuse, texcoord);
    specular *= texture2D(tex_specular, texcoord);
    
    //vec4 emissive = vec4(0, 0, 0, 1);
	//vec4 diffuse = vec4(0.5, 0.5, 0.5, 1)*brightness;
	//vec4 specular = vec4(0.9, 0.9, 0.9, 1)*spec;
	//fragColor.rgb = tangent;
    fragColor = diffuse + specular;
}
