<%-- 
    Document   : index
    Created on : Mar 3, 2026, 9:17:33 PM
    Author     : Yuikiri
--%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Medi-Care Hospital | Home</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <style>
            :root {
                --med-blue: #007bff;
                --med-dark: #343a40;
            }
            body {
                font-family: 'Segoe UI', sans-serif;
            }
            .navbar-brand {
                font-weight: 800;
                color: var(--med-blue);
            }
            .hero-section {
                background: linear-gradient(rgba(0,0,0,0.5), rgba(0,0,0,0.5)),
                    url('https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?auto=format&fit=crop&w=1350&q=80');
                background-size: cover;
                background-position: center;
                color: white;
                padding: 120px 0;
            }
            .service-card {
                transition: 0.3s;
                border: none;
                border-radius: 15px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            }
            .service-card:hover {
                transform: translateY(-10px);
            }
            .icon-box {
                font-size: 3rem;
                color: var(--med-blue);
                margin-bottom: 15px;
            }
            footer {
                background: var(--med-dark);
                color: #ccc;
                padding: 40px 0;
            }
        </style>
    </head>
    <body>

        <nav class="navbar navbar-light bg-white py-3">
            <div class="container">
                <a class="navbar-brand fs-3" href="index.jsp">
                    <i class="fas fa-hand-holding-medical"></i> MEDI-CARE
                </a>
                <div class="ms-auto d-flex gap-2">
                    <button type="button" class="btn btn-outline-primary px-4" data-bs-toggle="modal" data-bs-target="#loginModal">
                        Login
                    </button>                <button class="btn btn-primary px-4 shadow-sm" data-bs-toggle="modal" data-bs-target="#registerModal">
                        <i class="fas fa-user-plus me-2"></i> Register
                    </button>
                </div>
            </div>
        </nav>

        <nav class="navbar navbar-expand-lg navbar-dark bg-primary sticky-top shadow">
            <div class="container">
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="mainNav">
                    <ul class="navbar-nav mx-auto text-uppercase">
                        <li class="nav-item"><a class="nav-link active px-3" href="#">Home</a></li>
                        <li class="nav-item"><a class="nav-link px-3" href="#">About Us</a></li>
                        <li class="nav-item"><a class="nav-link px-3" href="#">Departments</a></li>
                        <li class="nav-item"><a class="nav-link px-3" href="#">Doctors</a></li>
                        <li class="nav-item"><a class="nav-link px-3" href="#">Contact</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <section class="hero-section text-center">
            <div class="container">
                <h1 class="display-3 fw-bold">Your Health, Our Mission</h1>
                <p class="lead mb-4 fs-4">Advanced care for a better life. Register now to manage your health records.</p>
                <button class="btn btn-primary btn-lg px-5 shadow" data-bs-toggle="modal" data-bs-target="#registerModal">Join Us Now</button>
            </div>
        </section>

        <section class="container my-5 py-5">
            <div class="row g-4 text-center">
                <div class="col-md-4">
                    <div class="card h-100 p-4 service-card">
                        <div class="icon-box"><i class="fas fa-user-md"></i></div>
                        <h4>Expert Doctors</h4>
                        <p class="text-muted">Access to world-class specialists in every field.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card h-100 p-4 service-card">
                        <div class="icon-box"><i class="fas fa-clock"></i></div>
                        <h4>24/7 Support</h4>
                        <p class="text-muted">Emergency services and support whenever you need.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card h-100 p-4 service-card">
                        <div class="icon-box"><i class="fas fa-notes-medical"></i></div>
                        <h4>Online Records</h4>
                        <p class="text-muted">View your medical history and prescriptions online.</p>
                    </div>
                </div>
            </div>
        </section>

        <div class="modal fade" id="registerModal" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title fw-bold">Create New Patient Account</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body p-4">
                        <form action="register" method="post">
                            <div class="mb-3">
                                <label class="form-label fw-bold">Full Name</label>
                                <input type="text" name="username" class="form-control" required placeholder="Enter your full name">
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold">Email Address</label>
                                <input type="email" name="email" class="form-control" required placeholder="example@mail.com">
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold">Password</label>
                                <input type="password" name="password" class="form-control" required placeholder="Min 8 characters">
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold">Phone Number</label>
                                <input type="text" name="phone" class="form-control" placeholder="E.g: 0987654321">
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold">Current Address</label>
                                <textarea name="address" class="form-control" rows="2" placeholder="Street, District, City..."></textarea>
                            </div>
                            <button type="submit" class="btn btn-primary w-100 py-2 fs-5 mt-2 shadow-sm">Sign Up</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <footer class="mt-auto">
            <div class="container text-center text-md-start">
                <div class="row gy-4">
                    <div class="col-md-6">
                        <h5 class="fw-bold text-white mb-4">MEDI-CARE HOSPITAL</h5>
                        <p><i class="fas fa-envelope text-primary me-2"></i> <b>Email:</b> contact@medicare.com</p>
                        <p><i class="fas fa-phone-alt text-primary me-2"></i> <b>Phone:</b> +84 xxx0 1xx4</p>
                        <p><i class="fas fa-map-marker-alt text-primary me-2"></i> 123 Healthcare Ave, Medical District</p>
                    </div>
                    <div class="col-md-6 text-md-end">
                        <h5 class="fw-bold text-white mb-4">Follow Us</h5>
                        <div class="fs-3 d-flex justify-content-md-end justify-content-center gap-3">
                            <a href="#" class="text-white"><i class="fab fa-facebook"></i></a>
                            <a href="#" class="text-white"><i class="fab fa-twitter"></i></a>
                            <a href="#" class="text-white"><i class="fab fa-linkedin"></i></a>
                        </div>
                    </div>
                </div>
                <hr class="my-4 border-secondary">
                <p class="text-center mb-0 small text-secondary">© 2026 Medi-Care Hospital System. All rights reserved.</p>
            </div>
        </footer>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
    <div class="modal fade" id="loginModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title fw-bold">Patient Login</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body p-4">
                    <form action="loginController" method="post">
                        <div class="mb-3">
                            <label class="form-label fw-bold">Username or Email</label>
                            <input type="text" name="txtUser" class="form-control" required placeholder="Enter your username">
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold">Password</label>
                            <input type="password" name="txtPass" class="form-control" required placeholder="Enter your password">
                        </div>
                        <div class="mb-3 form-check">
                            <input type="checkbox" class="form-check-input" id="rememberMe">
                            <label class="form-check-label" for="rememberMe">Remember me</label>
                        </div>
                        <button type="submit" class="btn btn-primary w-100 py-2 fs-5 mt-2 shadow-sm">Login</button>
                    </form>
                </div>
                <div class="modal-footer justify-content-center border-0">
                    <p class="mb-0 text-muted">Don't have an account? 
                        <a href="#" class="text-primary fw-bold text-decoration-none" data-bs-toggle="modal" data-bs-target="#registerModal">Register Now</a>
                    </p>
                </div>
            </div>
        </div>
    </div>
</html>
