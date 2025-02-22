#include <stdarg.h>
#include <stdbool.h>
#include <stdint.h>
#include <stdlib.h>

/**
 * Computes the sine of an input, x, in radians
 */
float sin_float(float x);

/**
 * Computes the cosine of an input, x, in radians
 */
float cos_float(float x);

/**
 * Computes the tangent of an input, x, in radians
 */
float tan_float(float x);

/**
 * Square roots the input x
 */
float sqrt_float(float x);

/**
 * Floors the input x
 */
int32_t floor_float(float x);

/**
 * Floors the input x
 */
int32_t floor_double(double x);

/**
 * Floor the input x
 */
int64_t floor_long(double x);

/**
 * Absolute value of the input x
 */
float abs_float(float x);

/**
 * Absolute value of the input x
 */
int32_t abs_int(int32_t x);

/**
 * Ceiling the input x
 */
int32_t ceil_float(float x);

/**
 * Ceiling the input x
 */
int32_t ceil_double(double x);

/**
 * Limits the input x between a minimum and maximium value
 */
int32_t clamp_int(int32_t x, int32_t min, int32_t max);

/**
 * Limits the input x between a minimum and maximium value
 */
int64_t clamp_long(int64_t x, int64_t min, int64_t max);

/**
 * Limits the input x between a minimum and maximium value
 */
float clamp_float(float x, float min, float max);

/**
 * Limits the input x between a minimum and maximium value
 */
double clamp_double(double x, double min, double max);

/**
 * Limits Linear Interpolation
 */
double clamp_lerp_double(double start, double end, double delta);

/**
 * Limits Linear Interpolation
 */
float clamp_lerp_float(float start, float end, float delta);

/**
 * Gets the largest value from the absolute value of a and b
 */
double abs_max(double a, double b);

/**
 * Divides then floors
 */
int32_t floor_div(int32_t dividend, int32_t divisor);

/**
 * If the values are equal excluding floating point errors
 */
bool approximately_equals_float(float a, float b);

/**
 * If the values are equal excluding floating point errors
 */
bool approximately_equals_double(double a, double b);

/**
 * Floor mod of the dividend and divisor
 */
int32_t floor_mod_int(int32_t dividend, int32_t divisor);

/**
 * Floor mod of the dividend and divisor
 */
float floor_mod_float(float dividend, float divisor);

/**
 * Floor mod of the dividend and divisor
 */
double floor_mod_double(double dividend, double divisor);

/**
 * If a is a multiple of b
 */
bool is_multiple_of(int32_t a, int32_t b);

/**
 * Compacts degrees into a single byte
 */
int8_t pack_degrees(float degrees);

/**
 * Converts bytes to degrees
 */
float unpack_degrees(int8_t packed_degrees);

/**
 * Forces degrees into +/- 180
 */
int32_t wrap_degrees_int(int32_t degrees);

/**
 * Forces degrees into +/- 180
 */
float wrap_degrees_long(int64_t degrees);

/**
 * Forces degrees into +/- 180
 */
float wrap_degrees_float(float degrees);

/**
 * Forces degrees into +/- 180
 */
double wrap_degrees_double(double degrees);

/**
 * Subtracts end from start
 */
float subtract_angles(float start, float end);

/**
 * Absolute value of second - first
 */
float angle_between(float first, float second);

/**
 * Clamps mean - value between the delta
 */
float clamp_angle(float value, float mean, float delta);

/**
 * Steps from towards to, only changing the amount by at most step
 */
float step_towards(float from, float to, float step);

/**
 * Steps from towards to, only changing the amount by at most step
 */
float step_unwrapped_angle_towards(float from, float to, float step);

/**
 * Gets the smallest power of two
 */
int32_t smallest_encompassing_power_of_two(int32_t value);

/**
 * Returns if the value is a power of two
 */
bool is_power_of_two(int32_t value);

/**
 * Takes the ceiling of the log2(value) using the de Bruijn sequence
 */
int32_t ceil_log_2(int32_t value);

/**
 * Takes the floor of log2(value) using the de Bruijn sequence
 */
int32_t floor_log_2(int32_t value);

/**
 * Gets the fractional part of a float
 */
float fractional_part_float(float value);

/**
 * Gets the fractional part of a float
 */
double fractional_part_double(double value);

/**
 * Generates a hash code
 */
int64_t hash_code(int32_t x, int32_t y, int32_t z);

/**
 * Gets the fraction of the way value is between start and end
 */
double get_lerp_progress_double(double value, double start, double end);

/**
 * Gets the fraction of the way value is between start and end
 */
float get_lerp_progress_float(float value, float start, float end);

/**
 * Approximates the atan2 function
 */
double atan_2(double y, double x);

/**
 * Gets the inverse of the square root of x
 */
float inverse_sqrt_float(float x);

/**
 * Gets the inverse of the square root of x
 */
double inverse_sqrt_double(double x);

/**
 * Approximation of 1 / cbrt(x)
 */
float fast_inverse_cbrt(float x);

/**
 * Converts HSV to RGB Values
 */
int32_t hsv_to_rgb(float hue, float saturation, float value);

/**
 * Converts HSV to ARGB values
 */
int32_t hsv_to_argb(float hue, float saturation, float value, int32_t alpha);

/**
 * Creates an ideal hash
 */
int32_t ideal_hash(int32_t value);

/**
 * Linear Interpolation f-rom start to end over delta time
 */
float lerp_float(float delta, float start, float end);

/**
 * Linear Interpolation from start to end over delta time
 */
double lerp_double(double delta, double start, double end);

/**
 * Linear Interpolation that always returns positive if delta is positive
 */
float lerp_positive(float delta, float start, float end);

/**
 * Two-dimensional Linear Interpolation
 */
double lerp_2(double deltax, double deltay, double x0y0, double x1y0, double x0y1, double x1y1);

/**
 * Three-dimensional Linear Interpolation
 */
double lerp_3(double delta_x,
              double delta_y,
              double delta_z,
              double x0y0z0,
              double x1y0z0,
              double x0y1z0,
              double x1y1z0,
              double x0y0z1,
              double x1y0z1,
              double x0y1z1,
              double x1y1z1);

/**
 * Interpolates a point on the Catmull-Rom Spline
 */
float catmull_rom(float delta, float p0, float p1, float p2, float p3);

/**
 * Fades a value using Perlin
 */
double perlin_fade(double value);

/**
 * Derivative of the Perlin Fade function
 */
double perlin_fade_derivative(double value);

/**
 * Gets the sign of a value
 */
int32_t sign(double value);

/**
 * Performs Linear Interpolation on an angle
 */
float lerp_angle_degrees_float(float delta, float start, float end);

/**
 * Performs Linear Interpolation on an angle
 */
double lerp_angle_degrees_double(double delta, double start, double end);

/**
 * Performs Linear Interpolation on an angle
 */
float lerp_angle_radians(float delta, float start, float end);

/**
 * Wraps a number around after it hits the max deviation
 */
float wrap(float value, float max_deviation);

/**
 * Squares a value
 */
float square_float(float value);

/**
 * Squares a value
 */
double square_double(double value);

/**
 * Squares a value
 */
int32_t square_int(int32_t value);

/**
 * Squares a value
 */
int64_t square_long(int64_t value);

/**
 * Linearly maps a value from one number range to another and clamps the result
 */
double clamped_map_double(double value,
                          double old_start,
                          double old_end,
                          double new_start,
                          double new_end);

/**
 * Linearly maps a value from one number range to another and clamps the result
 */
float clamped_map_float(float value,
                        float old_start,
                        float old_end,
                        float new_start,
                        float new_end);

/**
 * Linearly maps a value from one number range to another, unclamped
 */
double map_double(double value, double old_start, double old_end, double new_start, double new_end);

/**
 * Linearly maps a value from one number range to another, unclamped
 */
float map_float(float value, float old_start, float old_end, float new_start, float new_end);

/**
 * Returns a value farther than or as far as value from zero that is a multiple of divisor
 */
int32_t round_up_to_multiple(int32_t value, int32_t divisor);

/**
 * Divides then ceilings
 */
int32_t ceil_div(int32_t a, int32_t b);

/**
 * A^2 + B^2
 */
double squared_hypot(double a, double b);

/**
 * Gets the hypotenuse length
 */
double hypot_double(double a, double b);

/**
 * Gets the hypotenuse length
 */
float hypot_float(float a, float b);

/**
 * Gets the magnitude squared
 */
double squared_magnitude(double a, double b, double c);

/**
 * Gets the magnitude of the vector
 */
double magnitude_double(double a, double b, double c);

/**
 * Gets the magnitude of the vector
 */
float magnitude_float(float a, float b, float c);

/**
 * Returns a rounded down to the nearest multiple of b.
 */
int32_t round_down_to_multiple(float a, int32_t b);

/**
 * Gradual sine function
 */
float ease_in_out_sine(float value);
