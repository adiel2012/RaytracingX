# RaytracingX Example

This is an example image from the RaytracingX project:

git checkout basic-light-model:

![RaytracingX](https://github.com/adiel2012/RaytracingX/blob/main/simplelight.PNG?raw=true)

The above image showcases the output of the ray tracing algorithm with light model.



git checkout basic-light-model:

![RaytracingX](https://github.com/adiel2012/RaytracingX/blob/main/simpleobjects.PNG?raw=true)

The above image showcases the output of the ray tracing algorithm.


# Ray Tracing Algorithm Explanation

The code implements a basic ray tracing algorithm. Ray tracing is a rendering technique used in computer graphics to create realistic images by simulating the way light interacts with objects in a scene. Let's explore the theory behind ray tracing and the key formulas involved.

## Ray Tracing Theory

### Camera and Viewport
The camera is positioned at `cameraPosition`, and we create a virtual viewport through which rays are cast. The viewport's dimensions are determined by `viewportWidth` and `viewportHeight`.

### Ray Generation
For each pixel in the image, we generate a ray originating from the camera and passing through that pixel on the viewport.

### Ray-Object Intersection
We test each ray against all objects in the scene to determine if and where it intersects with any object.

### Shading
When an intersection is found, we determine the color of the pixel based on the properties of the intersected object and lighting conditions.

## Key Formulas

### Ray Equation
A ray is defined by its origin point (O) and direction vector (D):

$R(t) = O + tD$

Where:
- $R(t)$ is a point on the ray
- $O$ is the origin of the ray (camera position)
- $D$ is the normalized direction vector
- $t$ is a parameter ($t \geq 0$)

### Viewport to World Space Mapping
The code maps pixel coordinates to viewport coordinates:

$u = \frac{x - \text{width}/2}{\text{width}}$

$v = \frac{y - \text{height}/2}{\text{height}}$

These are then used to calculate the ray direction:

$\text{direction} = \text{normalize}(u \cdot \text{viewportWidth}, v \cdot \text{viewportHeight}, \text{lookAtPoint.Z})$

### Ray-Sphere Intersection
For a sphere with center C and radius r, the intersection with a ray is found by solving:

$|R(t) - C|^2 = r^2$

Expanding this equation gives a quadratic equation in t:

$(D \cdot D)t^2 + 2D \cdot (O - C)t + (O - C) \cdot (O - C) - r^2 = 0$

If this equation has real roots, the ray intersects the sphere.

### Ray-Plane Intersection
For a plane defined by a point P and normal N, the intersection is found by solving:

$(R(t) - P) \cdot N = 0$

This gives:

$t = \frac{(P - O) \cdot N}{D \cdot N}$

### Shading
In this basic implementation, shading is simplified to just returning the color of the intersected object. More advanced ray tracers would implement:

- **Diffuse reflection:** $I_d = k_d \cdot (N \cdot L) \cdot I_l$
- **Specular reflection:** $I_s = k_s \cdot (R \cdot V)^n \cdot I_l$
- **Shadows:** Cast secondary rays towards light sources
- **Reflections:** Cast secondary rays in the reflection direction
- **Refractions:** Use Snell's law to calculate refracted ray directions

Where:
- $I_d, I_s$ are diffuse and specular intensities
- $k_d, k_s$ are diffuse and specular coefficients
- $N$ is surface normal, $L$ is light direction
- $R$ is reflection vector, $V$ is view direction
- $I_l$ is light intensity
- $n$ is specular exponent

The provided code implements a basic version of this theory, focusing on ray generation and simple intersection tests. It could be extended to include more advanced shading, reflections, refractions, and other effects for more realistic rendering.
