uniform sampler2D TextureUnit0;
uniform sampler2D TextureUnit1;
uniform sampler2D TextureUnit2;
uniform sampler2D TextureUnit3;

varying vec3 normal;
varying vec3 tangent;
varying vec3 binormal;

varying vec3 vertex_light_half_vector;
varying vec3 vertex_light_position;

void main()
{
    vec3 N = normalize(normal);
    vec3 T = normalize(tangent);
    vec3 B = normalize(binormal);
    
    vec3 nmap;
    nmap = (texture2D(TextureUnit1, gl_TexCoord[0].st).rgb)*2.0;

    //N = normalize((T * nmap.x) + (B * nmap.y) + (N * nmap.z));
    
    
    
    vec4 ambient_color = gl_FrontMaterial.ambient * gl_LightSource[0].ambient + gl_LightModel.ambient * gl_FrontMaterial.ambient;

    vec4 diffuse_color = (texture2D(TextureUnit0, gl_TexCoord[0].st)) * gl_LightSource[0].diffuse;

    //vec4 specular_color = gl_FrontMaterial.specular * gl_LightSource[0].specular * pow(max(dot(N, vertex_light_half_vector), 0.0) , gl_FrontMaterial.shininess);
	//vec4 specular_color = vec4(1.0, 1.0, 1.0, 1.0) * gl_LightSource[0].specular * pow(max(dot(N, vertex_light_half_vector), 0.0) , 10.0);
	//specular_color = specular_color*dot(texture2D(TextureUnit2, gl_TexCoord[0].st).rgb, vec3(0.299, 0.587, 0.114));
	vec4 specular_color = gl_FrontMaterial.specular * gl_LightSource[0].specular*texture2D(TextureUnit2, gl_TexCoord[0].st).rgba*pow(max(dot(N, vertex_light_half_vector), 0.0) , 20);

    float brightness = dot(N, vertex_light_position) / (length(vertex_light_position) * length(N));
    brightness = clamp(brightness, 0, 1);
    
    vec4 emissive = texture2D(TextureUnit3, gl_TexCoord[0].st);

    //gl_FragColor = emissive + (diffuse_color+texture2D(TextureUnit0, gl_TexCoord[0].st))*diffuse_value + specular_color;
    gl_FragColor = ambient_color + emissive + diffuse_color*brightness + diffuse_color*specular_color;
}