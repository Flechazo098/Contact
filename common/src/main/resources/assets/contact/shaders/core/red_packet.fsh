#version 150

uniform float Time;
uniform vec2 Resolution;
uniform vec4 ColorModulator;
uniform float ToastAlpha;

in vec2 texCoord0;
out vec4 fragColor;

float hash(vec2 p) {
    p = fract(p * vec2(123.34, 345.45));
    p += dot(p, p + 34.23);
    return fract(p.x * p.y);
}

float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    float a = hash(i);
    float b = hash(i + vec2(1.0, 0.0));
    float c = hash(i + vec2(0.0, 1.0));
    float d = hash(i + vec2(1.0, 1.0));
    return mix(mix(a, b, f.x), mix(c, d, f.x), f.y);
}

vec3 palette(float t) {
    vec3 a = vec3(0.50, 0.06, 0.06);
    vec3 b = vec3(0.90, 0.15, 0.10);
    vec3 c = vec3(1.00, 0.70, 0.20);
    return mix(a, b, smoothstep(0.0, 1.0, t)) + c * pow(t, 3.0) * 0.2;
}

void main() {
    vec2 uv = texCoord0;
    vec2 centered = uv * 2.0 - 1.0;
    float aspect = Resolution.x / max(1.0, Resolution.y);

    float t = Time * 0.6;

    float grad = smoothstep(-1.0, 1.0, uv.y * 2.0 - 1.0);
    vec3 base = palette(grad);

    float paper = noise(uv * 10.0) * 0.04;
    base += paper;

    vec2 nuv = uv;
    nuv.x *= aspect;
    float swirl = noise(nuv * 6.0 + vec2(0.0, t * 0.3));
    vec3 gold = vec3(1.0, 0.85, 0.35);
    base += gold * smoothstep(0.6, 0.9, swirl) * 0.25;

    float spark = 0.0;
    for (int i = 0; i < 24; i++) {
        float fi = float(i);
        vec2 p = vec2(hash(vec2(fi, fi + 1.0)), hash(vec2(fi + 2.0, fi + 3.0)));
        p = (p - 0.5) * vec2(1.6, 1.0);
        float life = fract(t + hash(vec2(fi, fi * 3.1)));
        vec2 pos = mix(p, p + vec2(0.0, 0.2), life);
        float size = mix(0.012, 0.004, life);
        float d = length(centered - pos);
        spark += size / (d * d + 0.0008);
    }
    base += gold * spark * 0.6;

    float vignette = smoothstep(0.9, 0.3, length(centered * vec2(0.95, 0.85)));
    base *= vignette * 0.8 + 0.2;

    vec2 q = abs(centered) - vec2(1.0, 1.0) + 0.15;
    float dist = length(max(q, 0.0)) + min(max(q.x, q.y), 0.0);
    float softMask = 1.0 - smoothstep(0.0, 0.2, dist);

    float alpha = ToastAlpha * softMask * 0.9;
    fragColor = vec4(base * alpha, alpha) * ColorModulator;
}
