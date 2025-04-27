import Link from "next/link";

export default function Footer() {
  const currentYear = new Date().getFullYear();

  return (
    <footer style={{ backgroundColor: "rgb(0 75 73)" }} className="text-white">
      <div className="container mx-auto px-4 py-8">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          <div>
            <h3 className="text-lg font-bold mb-4">IUT Study Sync</h3>
            <p className="text-gray-200">
              Empowering students with the best learning environment and
              resources for academic excellence.
            </p>
          </div>

          <div>
            <h3 className="text-lg font-bold mb-4">Quick Links</h3>
            <ul className="space-y-2">
              <li>
                <Link
                  href="/"
                  className="text-gray-200 hover:text-white transition-colors"
                >
                  Home
                </Link>
              </li>
              <li>
                <Link
                  href="/about"
                  className="text-gray-200 hover:text-white transition-colors"
                >
                  About Us
                </Link>
              </li>
              <li>
                <Link
                  href="/courses"
                  className="text-gray-200 hover:text-white transition-colors"
                >
                  Courses
                </Link>
              </li>
              <li>
                <Link
                  href="/contact"
                  className="text-gray-200 hover:text-white transition-colors"
                >
                  Contact
                </Link>
              </li>
            </ul>
          </div>

          <div>
            <h3 className="text-lg font-bold mb-4">Resources</h3>
            <ul className="space-y-2">
              <li>
                <Link
                  href="/library"
                  className="text-gray-200 hover:text-white transition-colors"
                >
                  Library
                </Link>
              </li>
              <li>
                <Link
                  href="/research"
                  className="text-gray-200 hover:text-white transition-colors"
                >
                  Research
                </Link>
              </li>
              <li>
                <Link
                  href="/events"
                  className="text-gray-200 hover:text-white transition-colors"
                >
                  Events
                </Link>
              </li>
              <li>
                <Link
                  href="/faq"
                  className="text-gray-200 hover:text-white transition-colors"
                >
                  FAQ
                </Link>
              </li>
            </ul>
          </div>

          <div>
            <h3 className="text-lg font-bold mb-4">Contact Us</h3>
            <address className="not-italic text-gray-200">
              <p>Islamic University of Technology</p>
              <p>Board Bazar, Gazipur-1704</p>
              <p>Bangladesh</p>
              <p className="mt-2">Email: info@iut-dhaka.edu</p>
              <p>Phone: +880-2-9291254</p>
            </address>
          </div>
        </div>

        <div className="border-t border-gray-600 mt-8 pt-6 text-center">
          <p>&copy; {currentYear} IUT Study Sync. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
}
