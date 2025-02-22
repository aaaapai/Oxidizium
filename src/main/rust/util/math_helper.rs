use ape_table_trig::*;

const APPROXIMATION_THRESHOLD: f32 = 1.0E-5;
const PACKED_DEGREES_CONSTANT: f32 = 360.0 / 256.0;
const UNPACKED_DEGREES_CONSTANT: f32 = 256.0 / 360.0;
const MULTIPLY_DE_BRUIJN_BIT_POS: [i32; 32] = [
    0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9
];

static TABLE: [f32; 65536] = trig_table_gen_f32!(65536);

/// Computes the sine of an input, x, in radians
pub fn sin(x: f32) -> f32 {
    let table = TrigTableF32::new(&TABLE);
    table.sin(x)
}

/// Computes the cosine of an input, x, in radians
pub fn cos(x: f32) -> f32 {
    let table = TrigTableF32::new(&TABLE);
    table.cos(x)
}

/// Computes the tangent of an input, x, in radians
pub fn tan(x: f32) -> f32 {
    let table = TrigTableF32::new(&TABLE);
    table.tan(x)
}

/// Square roots the input x
pub fn sqrt(x: f32) -> f32 {
    x.sqrt()
}

/// Floors the input x
pub fn floor_float(x: f32) -> i32 {
    x.floor() as i32
}

/// Floors the input x
pub fn floor_double(x: f64) -> i32 {
    x.floor() as i32
}

/// Floor the input x
pub fn floor_long(x: f64) -> i64 {
    x.floor() as i64
}

/// Absolute value of the input x
pub fn abs_float(x: f32) -> f32 {
    x.abs()
}

/// Absolute value of the input x
pub fn abs_int(x: i32) -> i32 {
    x.abs()
}

/// Ceiling the input x
pub fn ceil_float(x: f32) -> i32 {
    x.ceil() as i32
}

/// Ceiling the input x
pub fn ceil_double(x: f64) -> i32 {
    x.ceil() as i32
}

/// Limits the input x between a minimum and maximium value
pub fn clamp_int(x: i32, min: i32, max: i32) -> i32 {
    x.clamp(min, max)
}

/// Limits the input x between a minimum and maximium value
pub fn clamp_long(x: i64, min: i64, max: i64) -> i64 {
    x.clamp(min, max)
}

/// Limits the input x between a minimum and maximium value
pub fn clamp_float(x: f32, min: f32, max: f32) -> f32 {
    x.clamp(min, max)
}

/// Limits the input x between a minimum and maximium value
pub fn clamp_double(x: f64, min: f64, max: f64) -> f64 {
    x.clamp(min, max)
}

/// Limits Linear Interpolation
pub fn clamp_lerp_double(start: f64, end: f64, delta: f64) -> f64 {
    if delta < 0.0 {
        start
    } else if delta > 1.0 {
        end
    } else {
        lerp_double(delta, start, end)
    }
}

/// Limits Linear Interpolation
pub fn clamp_lerp_float(start: f32, end: f32, delta: f32) -> f32 {
    if delta < 0.0 {
        start
    } else if delta > 1.0 {
        end
    } else {
        lerp_float(delta, start, end)
    }
}

/// Gets the largest value from the absolute value of a and b
pub fn abs_max(mut a: f64, mut b: f64) -> f64 {
    if a < 0.0 {
        a = -a;
    }

    if b < 0.0 {
        b = -b;
    }

    a.max(b)
}

/// Divides then floors
pub fn floor_div(dividend: i32, divisor: i32) -> i32 {
    (dividend as f32 / divisor as f32).floor() as i32
}

// TODO Implement `nextInt`, `nextFloat`, and `nextDouble`

/// If the values are equal excluding floating point errors
pub fn approximately_equals_float(a: f32, b: f32) -> bool {
    (b - a).abs() < APPROXIMATION_THRESHOLD
}

/// If the values are equal excluding floating point errors
pub fn approximately_equals_double(a: f64, b: f64) -> bool {
    (b - a).abs() < APPROXIMATION_THRESHOLD as f64
}

/// Floor mod of the dividend and divisor
pub fn floor_mod_int(dividend: i32, divisor: i32) -> i32 {
    let r: i32 = dividend % divisor;
    if (dividend ^ divisor) < 0 && r != 0 {
        r + divisor
    } else {
        r
    }
}

/// Floor mod of the dividend and divisor
pub fn floor_mod_float(dividend: f32, divisor: f32) -> f32 {
    (dividend % divisor + divisor) % divisor
}

/// Floor mod of the dividend and divisor
pub fn floor_mod_double(dividend: f64, divisor: f64) -> f64 {
    (dividend % divisor + divisor) % divisor
}

/// If a is a multiple of b
pub fn is_multiple_of(a: i32, b: i32) -> bool {
    a % b == 0
}

/// Compacts degrees into a single byte
pub fn pack_degrees(degrees: f32) -> i8 {
    (degrees * PACKED_DEGREES_CONSTANT).floor() as i8
}

/// Converts bytes to degrees
pub fn unpack_degrees(packed_degrees: i8) -> f32 {
    packed_degrees as f32 * UNPACKED_DEGREES_CONSTANT
}

/// Forces degrees into +/- 180
pub fn wrap_degrees_int(degrees: i32) -> i32 {
    ((degrees % 360) + 540) % 360 - 180
}

/// Forces degrees into +/- 180
pub fn wrap_degrees_long(degrees: i64) -> f32 {
    ((degrees % 360 as i64) as f32 + 540.0) % 360.0 - 180.0
}

/// Forces degrees into +/- 180
pub fn wrap_degrees_float(degrees: f32) -> f32 {
    ((degrees % 360.0) + 540.0) % 360.0 - 180.0
}

/// Forces degrees into +/- 180
pub fn wrap_degrees_double(degrees: f64) -> f64 {
    ((degrees % 360.0) + 540.0) % 360.0
}

/// Subtracts end from start
pub fn subtract_angles(start: f32, end: f32) -> f32 {
    wrap_degrees_float(end - start)
}

/// Absolute value of second - first
pub fn angle_between (first: f32, second: f32) -> f32 {
    subtract_angles(first, second).abs()
}

/// Clamps mean - value between the delta
pub fn clamp_angle(value: f32, mean: f32, delta: f32) -> f32 {
    let f: f32 = subtract_angles(value, mean);
    mean - clamp_float(f, -delta, delta)
}

/// Steps from towards to, only changing the amount by at most step
pub fn step_towards(from: f32, to: f32, mut step: f32) -> f32 {
    step = step.abs();
    if from < to {
        clamp_float(from + step, from, to)
    } else {
        clamp_float(from - step, to, from)
    }
}

/// Steps from towards to, only changing the amount by at most step
pub fn step_unwrapped_angle_towards(from: f32, to: f32, step: f32) -> f32 {
    step_towards(from, from + subtract_angles(from, to), step)
}

/// Parses the int.
/// If the parse fails, it reverts to fallback
pub fn parse_int(string: String, fallback: i32) -> i32 {
    i32::from_str(&string).unwrap_or(fallback)
}

/// Gets the smallest power of two
pub fn smallest_encompassing_power_of_two(value: i32) -> i32 {
    let mut i = value - 1;
    i |= i >> 1;
    i |= i >> 2;
    i |= i >> 4;
    i |= i >> 8;
    i |= i >> 16;
    i + 1
}

/// Returns if the value is a power of two
pub fn is_power_of_two(value: i32) -> bool {
    value != 0 && (value & (value - 1)) == 0
}

/// Takes the ceiling of the log2(value) using the de Bruijn sequence
pub fn ceil_log_2(mut value: i32) -> i32 {
    value = if is_power_of_two(value) {
        value
    } else {
        smallest_encompassing_power_of_two(value)
    };
    MULTIPLY_DE_BRUIJN_BIT_POS[(value as i64 * 125613361 >> 27) as i32 & 31]
}

/// Takes the floor of log2(value) using the de Bruijn sequence
pub fn floor_log_2(value: i32) -> i32 {
    ceil_log_2(value) - if is_power_of_two(value) {0} else {1}
}

/// Gets the fractional part of a float
pub fn fractional_part_float(value: f32) -> f32 {
    value.fract()
}

/// Gets the fractional part of a float
pub fn fractional_part_double(value: f64) -> f64 {
    value.fract()
}

/// Generates a hash code
pub fn hash_code(x: i32, y: i32, z: i32) -> i64 {
    let mut l: i64 = (x * 3129871) as i64 ^ (z as i64 * 116129781) ^ y as i64;
    l = l * l * 42317861 + l * 11 as i64;
    l >> 16
}

// TODO Generate random UUID

/// Gets the fraction of the way value is between start and end
pub fn get_lerp_progress_double(value: f64, start: f64, end: f64) -> f64 {
    (value - start) / (end - start)
}

/// Gets the fraction of the way value is between start and end
pub fn get_lerp_progress_float(value: f32, start: f32, end: f32) -> f32 {
    (value - start) / (end - start)
}

// TODO Implement method_34945 with Vec3d

/// Approximates the atan2 function
pub fn atan_2(mut y: f64, mut x: f64) -> f64 {
    let d: f64 = x * x + y * y;
    if d.is_nan() {
        f64::NAN
    } else {
        let bl: bool = y < 0.0;
        let bl2: bool = x < 0.0;

        if bl {
            y = -y;
        }

        if bl2 {
            x = -x;
        }

        let bl3: bool = y > x;
        if bl3 {
            let e: f64 = x;
            x = y;
            y = e;
        }

        // TODO Fast inverse square
    }
}

pub fn inverse_sqrt(x: f32) {
    
}


/// Linear Interpolation from start to end over delta time
pub fn lerp_float(delta: f32, start: f32, end: f32) -> f32 {
    start + (delta * (end - start))
}

/// Linear Interpolation from start to end over delta time
pub fn lerp_double(delta: f64, start: f64, end: f64) -> f64 {
    start + (delta * (end - start))
}

/// Linear Interpolation that always returns positive if delta is positive
pub fn lerp_positive(delta: f32, start: f32, end: f32) -> f32 {
    start + (delta * (end - start - 1.0)).floor() + if delta > 0.0 { 1.0 } else { 0.0 }
}

/// Two-dimensional Linear Interpolation
pub fn lerp_2(deltax: f64, deltay: f64, x0y0: f64, x1y0: f64, x0y1: f64, x1y1: f64) -> f64 {
    lerp_double(deltay, lerp_double(deltax, x0y0, x1y0), lerp_double(deltax, x0y1, x1y1))
}

/// Three-dimensional Linear Interpolation
pub fn lerp_3(
    delta_x: f64,
    delta_y: f64,
    delta_z: f64,
    x0y0z0: f64,
    x1y0z0: f64,
    x0y1z0: f64,
    x1y1z0: f64,
    x0y0z1: f64,
    x1y0z1: f64,
    x0y1z1: f64,
    x1y1z1: f64
) {
    lerp_double(delta_z, lerp_2(delta_x, delta_y, x0y0z0, x1y0z0, x0y1z0, x1y1z0), lerp_2(delta_x, delta_y, x0y0z1, x1y0z1, x0y1z1, x1y1z1));
}

