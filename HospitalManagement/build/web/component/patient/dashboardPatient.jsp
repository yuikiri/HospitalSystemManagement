<%-- 
    Document   : dashboardPatient
    Created on : Mar 3, 2026, 10:14:34 PM
    Author     : Yuikiri
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/style/dashbroad.css"/>
    </head>
    <body>
        <body class="bg-background text-foreground font-sans">
            <div class="container">
              <div class="flex min-h-screen w-full">
                <aside
                  id="sidebar"
                  class="fixed left-0 top-0 z-40 h-screen w-64 bg-gradient-to-b from-[#0c4a6e] to-[#075985] transition-all duration-300 ease-in-out"
                >
                  <div
                    class="flex h-20 items-center justify-between border-b border-white/10 px-4"
                  >
                    <div
                      id="sidebar-logo-container"
                      class="flex items-center gap-3 overflow-hidden transition-all opacity-100"
                    >
                      <div
                        class="flex h-12 w-12 shrink-0 items-center justify-center rounded-xl bg-gradient-to-br from-sky-400 to-blue-500 shadow-lg"
                      >
                        <span class="text-2xl">🏥</span>
                      </div>
                      <div id="sidebar-texts" class="whitespace-nowrap">
                        <h2 class="text-base text-white">MediCare HMS</h2>
                        <p class="text-xs text-sky-200">Front Office Portal</p>
                      </div>
                    </div>
                    <div class="flex h-12 w-12 shrink-0 items-center justify-center">
                      <button
                        id="toggle-sidebar-btn"
                        class="flex h-10 w-10 items-center justify-center rounded-md text-white hover:bg-white/10 cursor-pointer"
                      >
                        <i data-lucide="x" id="sidebar-toggle-icon" class="h-5 w-5"></i>
                      </button>
                    </div>
                  </div>

                  <nav class="space-y-1 p-3 pt-6">
                    <button
                      class="nav-item active group relative flex w-full items-center gap-3 rounded-xl px-3 py-3 text-sm transition-all duration-200 bg-gradient-to-r from-sky-400 to-blue-500 text-white shadow-lg shadow-sky-500/50"
                      data-target="dashboard"
                    >
                      <i data-lucide="home" class="h-5 w-5 shrink-0 animate-pulse"></i>
                      <span
                        class="nav-label flex-1 text-left font-medium transition-all opacity-100 whitespace-nowrap"
                        >Home</span
                      >
                      <i
                        data-lucide="chevron-right"
                        class="nav-arrow h-4 w-4 shrink-0"
                      ></i>
                    </button>

                    <button
                      class="nav-item group relative flex w-full items-center gap-3 rounded-xl px-3 py-3 text-sm transition-all duration-200 text-sky-100 hover:bg-white/10 hover:text-white"
                      data-target="about"
                    >
                      <i data-lucide="info" class="h-5 w-5 shrink-0"></i>
                      <span
                        class="nav-label flex-1 text-left font-medium transition-all opacity-100 whitespace-nowrap"
                        >About Us</span
                      >
                    </button>
                  </nav>

                  <div
                    class="absolute bottom-0 left-0 right-0 border-t border-white/10 p-4"
                  >
                    <div
                      id="sidebar-footer-content"
                      class="flex items-center gap-3 overflow-hidden opacity-100 transition-opacity"
                    >
                      <div
                        class="flex h-10 w-10 shrink-0 items-center justify-center rounded-full bg-sky-400/20"
                      >
                        <i data-lucide="users" class="h-5 w-5 text-sky-200"></i>
                      </div>
                      <div class="flex-1 overflow-hidden whitespace-nowrap">
                        <p class="text-xs text-white">Admin User</p>
                        <p class="truncate text-xs text-sky-200">admin@hospital.com</p>
                      </div>
                    </div>
                  </div>
                </aside>

                <main
                  id="main-wrapper"
                  class="flex-1 ml-64 transition-all duration-300 ease-in-out"
                >
                  <div class="sticky top-0 z-30 border-b bg-white/80 backdrop-blur-lg">
                    <div class="flex h-16 items-center gap-4 px-6">
                      <button
                        id="mobile-menu-btn"
                        class="lg:hidden flex h-10 w-10 items-center justify-center rounded-md hover:bg-muted cursor-pointer"
                      >
                        <i data-lucide="menu" class="h-5 w-5"></i>
                      </button>
                      <div class="flex-1">
                        <h1 class="text-base font-medium text-muted-foreground">
                          Hospital Management System
                        </h1>
                      </div>
                      <div class="flex items-center gap-2">
                        <div
                          class="hidden h-8 w-8 items-center justify-center rounded-full bg-primary/10 sm:flex"
                        >
                          <span class="text-xs text-primary font-medium">AU</span>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="p-6 md:p-8">
                    <div id="view-dashboard" class="page-view block space-y-6">
                      <div>
                        <h1 class="text-2xl font-medium">Dashboard</h1>
                        <p class="text-muted-foreground">
                          Welcome to Hospital Management System
                        </p>
                      </div>

                      <div class="grid gap-4 md:grid-cols-3">
                        <div
                          class="rounded-xl border bg-card text-card-foreground shadow-sm"
                        >
                          <div
                            class="p-6 flex flex-row items-center justify-between space-y-0 pb-2"
                          >
                            <h3 class="font-semibold tracking-tight text-sm">
                              Appointments Today
                            </h3>
                            <i data-lucide="calendar" class="h-4 w-4 text-blue-600"></i>
                          </div>
                          <div class="p-6 pt-0">
                            <div class="text-2xl font-semibold">24</div>
                            <p class="text-xs text-muted-foreground">
                              +3 from yesterday
                            </p>
                          </div>
                        </div>
                      </div>

                      <div
                        class="rounded-xl border bg-card text-card-foreground shadow-sm"
                      >
                        <div class="flex flex-col space-y-1.5 p-6">
                          <h3 class="font-semibold tracking-tight">Quick Actions</h3>
                          <p class="text-sm text-muted-foreground">
                            Frequently used functions
                          </p>
                        </div>
                        <div class="p-6 pt-0 grid gap-3 sm:grid-cols-3">
                          <button
                            class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring disabled:pointer-events-none disabled:opacity-50 bg-primary text-primary-foreground hover:bg-primary/90 h-auto flex-col gap-2 py-6 cursor-pointer"
                          >
                            <i data-lucide="plus" class="h-6 w-6"></i>
                            <span>Schedule New Appointment</span>
                          </button>
                          <button
                            class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring border border-input bg-background hover:bg-accent hover:text-accent-foreground h-auto flex-col gap-2 py-6 cursor-pointer"
                          >
                            <i data-lucide="user-check" class="h-6 w-6"></i>
                            <span>Check-In Patient</span>
                          </button>
                        </div>
                      </div>
                    </div>

                    <div id="view-about" class="page-view hidden space-y-6">
                      <div>
                        <h1 class="text-2xl font-medium">About Us</h1>
                        <p class="text-muted-foreground">
                          Hospital information and internal resources
                        </p>
                      </div>
                    </div>
                  </div>
                </main>
              </div>
            </div>

            <!-- <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script> -->
          </body>
    </body>
</html>
