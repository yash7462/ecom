<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<!-- www.ecom.com -->
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login & Signup | Ecom</title>
    <!-- Google Fonts Link For Icons -->
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@48,400,0,0">
    <link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/css/style.css" />
    <script src="<%=request.getContextPath()%>/js/script.js" type="text/javascript" defer></script>
</head>
<body>
    <header>
        <nav class="navbar">
            <span class="hamburger-btn material-symbols-rounded">menu</span>
            <a href="#" class="logo">
                <img src="<%=request.getContextPath()%>/images/logo.jpg" alt="logo">
                <h2>Veda Ecom</h2>
            </a>
            <ul class="links">
                <span class="close-btn material-symbols-rounded">close</span>
                <li><a href="#">Home</a></li>
                <li><a href="#">Shop</a></li>
                <li><a href="#">Category</a></li>
                <li><a href="#">About us</a></li>
                <li><a href="#">Contact us</a></li>
            </ul>
            <button class="login-btn main-btn" id="main-login-btn">LOG IN</button>
            <button class="logout-btn main-btn" id="main-logout-btn" style="display: none;">LOG OUT</button>
        </nav>
    </header>

    <div class="blur-bg-overlay">

    </div>

    <div class="form-popup">
        <span class="close-btn material-symbols-rounded">close</span>
        <div class="form-box login">
            <div class="form-details">
                <h2>Welcome Back</h2>
                <p>Please log in using your personal information to View Your Orders.</p>
            </div>
            <div class="form-content">
                <h2>LOGIN</h2>
                <form id="loginForm" action="/api/v1/auth/login">
                    <div class="input-field">
                        <input type="text" name="loginEmail" id="loginEmail" required>
                        <label>Email</label>
                    </div>
                    <div class="input-field">
                        <input type="password" name="loginPassword" id="loginPassword" required>
                        <label>Password</label>
                    </div>
                    <a href="#" class="forgot-pass-link">Forgot password?</a>
                    <button type="button" id="loginBtn">Log In</button>
                    <div id="loginError" style="color: red; margin-bottom: 5px; text-align: center; display: none;"></div>
                </form>
                <div class="bottom-link">
                    Don't have an account?
                    <a href="#" id="signup-link">Signup</a>
                </div>
            </div>
        </div>

        <!-- Sign Up -->
        <div class="form-box signup">
            <div class="form-details">
                <h2>Create Account</h2>
                <p>To become a part of our community, please sign up using your personal information.</p>
            </div>
            <div class="form-content">
                <h2>SIGNUP</h2>
                <form id="signupForm" action="/api/v1/admin/signup">
                    <div class="input-field">
                        <input type="text" name="signupEmail" id="signupEmail"  required>
                        <label>Enter your email</label>
                    </div>
                    <div class="input-field">
                        <input type="password" name="signupPassword" id="signupPassword"  required>
                        <label>Create password</label>
                    </div>
                    <div class="policy-text">
                        <input type="checkbox" id="policy">
                        <label for="policy">
                            I agree the
                            <a href="#" class="option">Terms & Conditions</a>
                        </label>
                    </div>
                    <button type="button" id="signupBtn">Sign Up</button>
                </form>
                <div class="bottom-link">
                    Already have an account?
                    <a href="#" id="login-link">Login</a>
                </div>
            </div>
        </div>
    </div>
</body>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.js" integrity="sha512-+k1pnlgt4F1H8L7t3z95o3/KO+o78INEcXTbnoJQ/F2VqDVhWoaiVml/OEHv9HsVgxUaVW+IbiZPUJQfF/YxZw==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script type="text/javascript">
    $(document).ready(function () {
        checkUserLoggedInOrNot();
        console.log("Main Page Loaded");
        hideError();

        $("#main-logout-btn").click(function() {
            localStorage.removeItem("jwtToken");
            localStorage.removeItem("user-details");
            checkUserLoggedInOrNot();
        });

        $("#loginBtn").click(function() {
           if($('#loginEmail').val() == undefined || $('#loginEmail').val() == null || $('#loginEmail').val() == "") {
                showError("Email Id Required");
                return false;
           }
           if($('#loginPassword').val() == undefined || $('#loginPassword').val() == null || $('#loginPassword').val() == "") {
               showError("Password Required");
               return false;
           }

            // Get form data
            var formData = {
                email: $('#loginEmail').val(),
                password: $('#loginPassword').val()
            };

            // Clear previous errors
            $('#loginError').hide().text('');

            // Make AJAX call
            $.ajax({
                type: 'POST',
                url: '${pageContext.request.contextPath}/api/v1/auth/login',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                success: function(response) {
                    if (response.status === 200) {
                        // Store token in localStorage
                        localStorage.setItem('jwtToken', response.data.token);
                        console.log('Login successful, token stored');

                        checkUserLoggedInOrNot();
                        // Redirect to dashboard or home page
                        window.location.href = '${pageContext.request.contextPath}/';
                    } else {
                        showError('Login failed: ' + response.message);
                    }
                },
                error: function(xhr) {
                    var errorMessage = 'Login failed. Please try again.';
                    if (xhr.responseJSON && xhr.responseJSON.message) {
                        errorMessage = xhr.responseJSON.message;
                    }
                    showError(errorMessage);
                }
            });
        });

    });
    // Function to show error messages
    function showError(message) {
        $('#loginError').text(message).show();
    }
    function hideError() {
        $('#loginError').text("").hide();
    }

    function checkUserLoggedInOrNot() {
        var token = localStorage.getItem('jwtToken');

        if (token) {
            $.ajax({
                type: 'POST',
                url: '${pageContext.request.contextPath}/api/v1/auth/login-user',
                contentType: 'application/json',
                success: function(response) {
                    if (response.status === 200) {
                        // Store Data in localStorage
                        localStorage.setItem('user-details', response.data);
                        console.log('User Verified Successful');

                        hideMainLoginBtn();
                    } else {
                        showMainLoginBtn();
                    }
                },
                error: function(xhr) {
                    var errorMessage = 'Login failed. Please try again.';
                    if (xhr.responseJSON && xhr.responseJSON.message) {
                        errorMessage = xhr.responseJSON.message;
                    }
                    showMainLoginBtn();
                }
            });
        } else {
            showMainLoginBtn();
        }


    }

    function showMainLoginBtn() {
        $('#main-logout-btn').hide();
        $('#main-login-btn').show();
    }
    function hideMainLoginBtn() {
        $('#main-login-btn').hide();
        $('#main-logout-btn').show();
    }

    // Add this function to include the token in all future AJAX requests
    $(document).ajaxSend(function(event, xhr) {
        var token = localStorage.getItem('jwtToken');
        if (token) {
            xhr.setRequestHeader('Authorization', 'Bearer ' + token);
        }
    });
</script

</html>