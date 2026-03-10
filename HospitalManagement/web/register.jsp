<%-- 
    Document   : register
    Created on : Mar 5, 2026, 4:58:58 PM
    Author     : Dang Khoa
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
        :root { --med-blue: #007bff; --med-dark: #343a40; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
        .navbar-brand { font-weight: 800; color: var(--med-blue); }
        .hero-section { 
            background: linear-gradient(rgba(255,255,255,0.8), rgba(255,255,255,0.8)), url('https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80');
            background-size: cover;
            padding: 100px 0; 
        }
        .service-card { transition: 0.3s; border: none; border-radius: 15px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); }
        .service-card:hover { transform: translateY(-10px); }
        .icon-box { font-size: 3rem; color: var(--med-blue); margin-bottom: 15px; }
        footer { background: var(--med-dark); color: #ccc; padding: 40px 0; }
    </style>
</head>
<body>

    <nav class="navbar navbar-light bg-white py-3">
        <div class="container">
            <a class="navbar-brand fs-3" href="#"><i class="fas fa-hand-holding-medical"></i> MEDI-CARE</a>
            <div class="ms-auto">
                <button class="btn btn-primary px-4 shadow-sm" data-bs-toggle="modal" data-bs-target="#registerModal">
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
                    <li class="nav-item"><a class="nav-link px-3" href="#">Doctors</a></li>
                    <li class="nav-item"><a class="nav-link px-3" href="#">Contact</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <section class="hero-section text-center">
        <div class="container">
            <h1 class="display-4 fw-bold">Your Health Is Our Top Priority</h1>
            <p class="lead mb-4">Register an account to book your appointment online today.</p>
        </div>
    </section>

    <div class="modal fade" id="registerModal" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title fw-bold">Patient Registration</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body p-4">
                    <form action="register" method="post">
                        <div class="mb-3">
                            <label class="form-label">Full Name</label>
                            <input type="text" name="username" class="form-control" required placeholder="Enter full name">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Email</label>
                            <input type="email" name="email" class="form-control" required placeholder="name@example.com">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Password</label>
                            <input type="password" name="password" class="form-control" required placeholder="********">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Phone Number</label>
                            <input type="text" name="phone" class="form-control" placeholder="0123456789">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Address</label>
                            <textarea name="address" class="form-control" rows="2" placeholder="Your current address"></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary w-100 py-2 mt-2">Create Account</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <footer class="mt-5">
        <div class="container text-center text-md-start">
            <div class="row">
                <div class="col-md-6 mb-3">
                    <h5 class="fw-bold text-white">CONTACT US</h5>
                    <p><i class="fas fa-envelope text-primary me-2"></i> contact@medicare.com</p>
                    <p><i class="fas fa-phone text-primary me-2"></i> +84 1900 1234</p>
                </div>
                <div class="col-md-6 text-md-end">
                    <p>© 2026 Medi-Care Hospital. All rights reserved.</p>
                </div>
            </div>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
