package server;

/*
 * Real-time and concurrent programming
 *
 * Minimalistic HTTP server solution.
 *
 * Package created by Patrik Persson, maintained by klas@cs.lth.se
 * Adapted for Axis cameras by Roger Henriksson 
 */

import java.net.*; // Provides ServerSocket, Socket
import java.io.*; // Provides InputStream, OutputStream

import se.lth.cs.eda040.fakecamera.*; // Provides AxisM3006V

/**
 * Itsy bitsy teeny weeny web server. Always returns an image, regardless of the
 * requested file name.
 */
public class JPEGHTTPServer extends Thread {

	// ------------------------------------------------------------ CONSTRUCTOR

	/**
	 * @param port
	 *            The TCP port the server should listen to
	 */
	ServerMonitor m;

	public JPEGHTTPServer(int port, ServerMonitor m) {
		super();
		this.m = m;
		myPort = port;
	}

	// --------------------------------------------------------- PUBLIC METHODS

	/**
	 * This method handles client requests. Runs in an eternal loop that does
	 * the following:
	 * <UL>
	 * <LI>Waits for a client to connect
	 * <LI>Reads a request from that client
	 * <LI>Sends a JPEG image from the camera (if it's a GET request)
	 * <LI>Closes the socket, i.e. disconnects from the client.
	 * </UL>
	 * 
	 * Two simple help methods (getLine/putLine) are used to read/write entire
	 * text lines from/to streams. Their implementations follow below.
	 */
	@SuppressWarnings("resource")
	public void run() {
		byte[] jpeg = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(myPort);
		} catch (IOException e1) {
			System.out
					.println("Failed to connect to server on port: " + myPort);
			return;
		}
		System.out.println("HTTP server operating at port " + myPort + ".");

		while (true) {
			try {
				// The 'accept' method waits for a client to connect, then
				// returns a socket connected to that client.
				Socket clientSocket = serverSocket.accept();

				// The socket is bi-directional. It has an input stream to read
				// from and an output stream to write to. The InputStream can
				// be read from using read(...) and the OutputStream can be
				// written to using write(...). However, we use our own
				// getLine/putLine methods below.
				InputStream is = clientSocket.getInputStream();
				OutputStream os = clientSocket.getOutputStream();

				// Read the request
				String request = getLine(is);

				// The request is followed by some additional header lines,
				// followed by a blank line. Those header lines are ignored.
				String header;
				boolean cont;
				do {
					header = getLine(is);
					cont = !(header.equals(""));
				} while (cont);

				System.out.println("HTTP request '" + request + "' received.");

				// Interpret the request. Complain about everything but GET.
				// Ignore the file name.
				if (request.substring(0, 4).equals("GET ")) {
					// Got a GET request. Respond with a JPEG image from the
					// camera. Tell the client not to cache the image
					putLine(os, "HTTP/1.0 200 OK");
					putLine(os, "Content-Type: image/jpeg");
					putLine(os, "Pragma: no-cache");
					putLine(os, "Cache-Control: no-cache");
					putLine(os, ""); // Means 'end of header'

					jpeg = m.getImage();

					byte[] image = new byte[jpeg.length-13];
					System.arraycopy(jpeg, 13, image, 0, jpeg.length-13);

					os.write(image);

				} else {
					// Got some other request. Respond with an error message.
					putLine(os, "HTTP/1.0 501 Method not implemented");
					putLine(os, "Content-Type: text/plain");
					putLine(os, "");
					putLine(os, "No can do. Request '" + request
							+ "' not understood.");

					System.out.println("Unsupported HTTP request!");
				}

				os.flush(); // Flush any remaining content
				clientSocket.close(); // Disconnect from the client
			} catch (IOException e) {
				System.out.println("Caught exception " + e);
			}
		}
	}

	// -------------------------------------------------------- PRIVATE METHODS

	/**
	 * Read a line from InputStream 's', terminated by CRLF. The CRLF is not
	 * included in the returned string.
	 */
	private static String getLine(InputStream s) throws IOException {
		boolean done = false;
		String result = "";

		while (!done) {
			int ch = s.read(); // Read
			if (ch <= 0 || ch == 10) {
				// Something < 0 means end of data (closed socket)
				// ASCII 10 (line feed) means end of line
				done = true;
			} else if (ch >= ' ') {
				result += (char) ch;
			}
		}

		return result;
	}

	/**
	 * Send a line on OutputStream 's', terminated by CRLF. The CRLF should not
	 * be included in the string str.
	 */
	private static void putLine(OutputStream s, String str) throws IOException {
		s.write(str.getBytes());
		s.write(CRLF);
	}

	// ----------------------------------------------------- PRIVATE ATTRIBUTES

	private int myPort; // TCP port for HTTP server

	// By convention, these bytes are always sent between lines
	// (CR = 13 = carriage return, LF = 10 = line feed)

	private static final byte[] CRLF = { 13, 10 };
}