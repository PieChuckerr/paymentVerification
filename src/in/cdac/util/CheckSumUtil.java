package in.cdac.util;

import java.security.MessageDigest;

public class CheckSumUtil
{
		public static void main(String arg[]) { }

		public static String checkSumMD5(String plaintext)
		{

				MessageDigest md = null;

				try
				{
						md = MessageDigest.getInstance("MD5");
						md.update(plaintext.getBytes("UTF-8"));
				}
				catch (Exception e)
				{
						md = null;
				}

				StringBuffer ls_sb = new StringBuffer();
				byte raw[] = md.digest(); // step 4

				for (int i = 0; i < raw.length; i++)
				{
						ls_sb.append(char2hex(raw[i]));
				}
				return ls_sb.toString().toLowerCase(); // step 6
		}

		public static String checkSumSHA256(String plaintext){

				MessageDigest md = null;
				try
				{
						md = MessageDigest.getInstance("SHA-256"); // step 2
						md.update(plaintext.getBytes("UTF-8")); // step 3
				}
				catch (Exception e)
				{
						md = null;
				}

				StringBuffer ls_sb = new StringBuffer();
				byte raw[] = md.digest(); // step 4

				for (int i = 0; i < raw.length; i++)
				{
						ls_sb.append(char2hex(raw[i]));
				}

				return ls_sb.toString(); // step 6
		}

		public static String char2hex(byte x){

				char arr[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
				char c[] = { arr[(x & 0xF0) >> 4], arr[x & 0x0F] };
				return (new String(c));
		}
}
