#version 460 core


struct ColorPoint{
	float value;
	vec3 rgb;
};

const ColorPoint[] points = ColorPoint[8](
										ColorPoint(0.0, vec3(25, 24, 23)),
        								ColorPoint(0.03, vec3(120, 90, 70)),
        								ColorPoint(0.05, vec3(130, 24, 23)),
        								ColorPoint(0.25, vec3(250, 179, 100)),
        								ColorPoint(0.5, vec3(43, 65, 98)),
        								ColorPoint(0.85, vec3(11, 110, 79)),
        								ColorPoint(0.95, vec3(150, 110, 79)),
        								ColorPoint(1.0, vec3(255, 255, 255)));

in vec2 spaceCoords;

uniform int max_iterations;

uniform float scale;
uniform vec2 render_size;

out vec4 out_Color;

vec2 compute_next(vec2 current, vec2 constant) {
	float zr = current.x * current.x - current.y * current.y + constant.x;
	float zi = 2.0 * current.x * current.y + constant.y;
	return vec2(zr, zi);
}

float mod2(vec2 z) {
	return z.x * z.x + z.y * z.y;
}

float log2(float v) {
	return log(v) / log(2);
}

float compute_iterations(vec2 z0, int max_iterations) {
	vec2 zn = vec2(z0);
	int iteration = 0;
	while (mod2(zn) < 4.0 && iteration < max_iterations) {
		zn = compute_next(zn, z0);
		iteration++;
	}
	float mod = length(zn);
	float smooth_iteration = iteration - log2(max(1.0, log2(mod)));
	return smooth_iteration;
}

vec3 get_color(float value){
	if (value >= 1.0f) {
		return points[7].rgb;
    } else if (value <= 0.0f) {
        return points[0].rgb;
    }
    
    for (int i = 0; i<points.length(); i++) {
    	ColorPoint cp = points[i];
        if (cp.value > value) {
            float range = cp.value - points[i-1].value;
            float pos = value - points[i-1].value;
            float ratio = pos / range;
        	return (1.0 - ratio) * points[i-1].rgb + ratio * cp.rgb;
        }
    }
    return vec3(255.0, 255.0, 255.0);
}

void main(){
	float value = compute_iterations(spaceCoords, max_iterations) / float(max_iterations);
	out_Color = vec4(get_color(value)/255.0, 1.0);
}

