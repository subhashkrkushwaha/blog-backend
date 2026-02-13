Blog App Backend
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Blog App Backend</title>
  <style>
    body { font-family: Arial, sans-serif; line-height: 1.6; margin: 20px; }
    h1, h2, h3 { color: #2c3e50; }
    code { background-color: #f4f4f4; padding: 2px 5px; border-radius: 3px; }
    pre { background-color: #f4f4f4; padding: 10px; border-radius: 5px; overflow-x: auto; }
    table { border-collapse: collapse; width: 100%; margin-bottom: 20px; }
    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
    th { background-color: #f2f2f2; }
    ul { margin: 0; padding-left: 20px; }
  </style>
</head>
<body>

<h1>Blog App Backend</h1>

<h2>Overview</h2>
<p><strong>Blog App Backend</strong> is a <strong>Spring Boot-based RESTful application</strong> that provides a <strong>secure, scalable, and role-based API</strong> for managing blogs. It supports <strong>user registration, authentication, blog creation, retrieval, and deletion</strong> with <strong>role-based access control</strong>.</p>
<p>The application uses <strong>MySQL</strong> for persistent storage, <strong>Redis caching</strong> for performance, and includes <strong>Swagger API documentation</strong> along with <strong>unit tests</strong> using <strong>JUnit</strong> and <strong>Mockito</strong>.</p>

<h2>Key Features</h2>

<h3>User Management</h3>
<ul>
  <li>User registration and login with secure password handling.</li>
  <li>JWT-based authentication for stateless security.</li>
  <li>Role-based authorization: <code>USER</code> and <code>ADMIN</code>.</li>
</ul>

<h3>Blog Management</h3>
<ul>
  <li>Create your own blog posts.</li>
  <li>View blogs created by others.</li>
  <li>Delete only your own blogs (enforced by security rules).</li>
</ul>

<h3>Security</h3>
<ul>
  <li>Spring Security integration with JWT authentication.</li>
  <li>Fine-grained role-based access control for sensitive operations.</li>
</ul>

<h3>Caching</h3>
<ul>
  <li>Redis caching for frequently accessed blog data to improve performance.</li>
</ul>

<h3>API Documentation</h3>
<ul>
  <li>Swagger UI for easy API testing and documentation.</li>
</ul>

<h3>Testing</h3>
<ul>
  <li>Unit tests using JUnit.</li>
  <li>Mockito for mocking dependencies in service and controller layers.</li>
</ul>

<h2>Technology Stack</h2>
<table>
  <tr>
    <th>Layer</th>
    <th>Technology</th>
  </tr>
  <tr>
    <td>Backend</td>
    <td>Spring Boot</td>
  </tr>
  <tr>
    <td>Security</td>
    <td>Spring Security, JWT</td>
  </tr>
  <tr>
    <td>Database</td>
    <td>MySQL (local)</td>
  </tr>
  <tr>
    <td>Caching</td>
    <td>Redis</td>
  </tr>
  <tr>
    <td>ORM</td>
    <td>Spring Data JPA / Hibernate</td>
  </tr>
  <tr>
    <td>API Docs</td>
    <td>Swagger</td>
  </tr>
  <tr>
    <td>Testing</td>
    <td>JUnit, Mockito</td>
  </tr>
  <tr>
    <td>Build Tool</td>
    <td>Maven</td>
  </tr>
</table>

<h2>Setup Instructions</h2>
<ol>
  <li><strong>Clone the repository:</strong>
    <pre>git clone &lt;your-repo-url&gt;</pre>
  </li>
  <li><strong>Configure MySQL database:</strong>
    <ul>
      <li>Create a database (e.g., <code>blog_db</code>).</li>
      <li>Update <code>application.properties</code> with your MySQL credentials.</li>
    </ul>
  </li>
  <li><strong>Run Redis</strong> locally or via Docker:
    <pre>docker run --name redis -p 6379:6379 -d redis</pre>
  </li>
  <li><strong>Start the application:</strong>
    <pre>mvn spring-boot:run</pre>
  </li>
  <li><strong>Access Swagger UI:</strong>
    <pre>http://localhost:8080/swagger-ui/index.html</pre>
  </li>
</ol>

<h2>Highlights for Resume / Portfolio</h2>
<ul>
  <li>Developed a <strong>secure RESTful backend</strong> using Spring Boot, JPA, and MySQL.</li>
  <li>Implemented <strong>JWT authentication</strong> and <strong>role-based authorization</strong>.</li>
  <li>Integrated <strong>Redis caching</strong> for performance optimization.</li>
  <li>Documented APIs using <strong>Swagger</strong>.</li>
  <li>Ensured high code quality with <strong>JUnit</strong> and <strong>Mockito</strong>.</li>
  <li>Enforced ownership rules: users can delete only their own blogs.</li>
</ul>

</body>
</html>
