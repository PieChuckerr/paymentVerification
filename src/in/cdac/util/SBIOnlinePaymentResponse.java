package in.cdac.util;

import in.cdac.bean.SBIPaymentResponseBean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.naming.NamingException;














import Encryption.AESEncrypt;

public class SBIOnlinePaymentResponse
{
		private static final long serialVersionUID = 1L;

		private String encdata;

		private boolean verifyCheckSums(String decryptedResponseData)
		{
				String respParamsWithoutCheckSum = decryptedResponseData.substring(0, decryptedResponseData.indexOf("checkSum") - 1);
				String respCheckSum = decryptedResponseData.substring(decryptedResponseData.indexOf("checkSum"));
				respCheckSum = respCheckSum.substring(respCheckSum.indexOf("=") + 1);

				String localComputedCheckSum = CheckSumUtil.checkSumMD5(respParamsWithoutCheckSum);

				return localComputedCheckSum.equals(respCheckSum);
		}

		private String decryptResponse(String encryptedResponse) throws URISyntaxException, IOException
		{
			AESEncrypt aesEncrypt = new AESEncrypt();
			
			Path file = Files.createTempFile(null, ".KEY");
			try (InputStream stream = this.getClass().getResourceAsStream("/GATE_IIT.KEY")) {
			   	Files.copy(stream, file, StandardCopyOption.REPLACE_EXISTING);
			}

			aesEncrypt.setSecretKey(file.toString());

				return aesEncrypt.decryptFile(encryptedResponse+"\n");
		}

		private String encryptRequest(String plainTextRequest) throws NullPointerException
		{
			try{
				if(plainTextRequest == null)
				{
						throw new NullPointerException("Request data found to be null! Can not encrypt");
				}

				AESEncrypt aesEncrypt = new AESEncrypt();
				
				Path file = Files.createTempFile(null, ".KEY");
				try (InputStream stream = this.getClass().getResourceAsStream("/GATE_IIT.KEY")) {
				   	Files.copy(stream, file, StandardCopyOption.REPLACE_EXISTING);
				}
				aesEncrypt.setSecretKey(file.toString());
				return aesEncrypt.encryptFile(plainTextRequest);
				//System.out.println("String to be encrypted: "+plainTextRequest+"\n");
			}
			catch(Exception e){
				e.printStackTrace();
			}
				return null;
		}

		private String prepareDoubleVerificationReqData(SBIPaymentResponseBean sbiPaymentResponseBean)
		{
				String reqString = "payment_Id=" + urlEncodeParam("" + sbiPaymentResponseBean.getPayment_Id())
								 + "|amount=" + urlEncodeParam(""+sbiPaymentResponseBean.getAmount().intValueExact())
								 + "|currencytype="+urlEncodeParam(""+sbiPaymentResponseBean.getCurrencyType());
				return reqString + "|checkSum=" + CheckSumUtil.checkSumMD5(reqString);
		}

		public String urlEncodeParam(String param) {

				if(param == null) {
						return null;
				}
				try {
						return URLEncoder.encode(param, "UTF-8");
				}
				catch (UnsupportedEncodingException e) {
						System.out.println("GTDG:XXX: UnsupportedEncodingException occurred in urlEncodeParam!");
						return null;
				}
		}

		private void populateResponseBean(String pipeDelimitedText, SBIPaymentResponseBean sbiResBean){

				//System.out.println("String from SBI: "+pipeDelimitedText+"\n");

				String[] tokens = pipeDelimitedText.split("\\|");
				int i = 0;
				while (i < tokens.length){

						try{

								String token = tokens[i].trim();
								String[] tk = token.split("=");

								tk[0] = tk[0].trim();
								tk[1] = tk[1].trim();

								if (tk[0].equals("amount"))
								{
										sbiResBean.setAmount(new BigDecimal( tk[1]) );
								}
								else if (tk[0].equals("bank_name"))
								{
										sbiResBean.setBank_name( tk[1] );

								}
								else if (tk[0].equals("payment_Id"))
								{
										sbiResBean.setPayment_Id( Integer.parseInt(tk[1]) );

								}
								else if (tk[0].equals("status"))
								{
										sbiResBean.setStatus( tk[1] );

								}
								else if (tk[0].equals("status_desc"))
								{
										sbiResBean.setStatus_desc( tk[1] );

								}
								else if (tk[0].equals("ttype"))
								{
										sbiResBean.setTtype( tk[1] );

								}
								else if (tk[0].equals("sbi_ref_no"))
								{
										sbiResBean.setSbi_ref_no( tk[1] );
								}
								else if (tk[0].equals("timestamp")) 
								{
										sbiResBean.setTimestamp( tk[1] );
								}
								else if( tk[0].equals("currencytype") )
								{
										sbiResBean.setCurrencyType( tk[1] );
								}

						}catch(Exception e){
								//e.printStackTrace();
						}
						i++;
				}
		}

		public SBIPaymentResponseBean paymentDoubleVerification(String feeAmount, String paymentId, String merchantCode, String currencyType, String dblVerificationURL){
				try
				{
						
						HttpClient httpClient = new HttpClient();

						String reqString = "payment_Id=" + urlEncodeParam(paymentId) + "|amount=" + urlEncodeParam(feeAmount)+"|currencytype="+urlEncodeParam(currencyType); 
						reqString = reqString + "|checkSum=" + CheckSumUtil.checkSumMD5(reqString);
						
						String encryptedDblVerificationReqData = encryptRequest(reqString);
						
						encryptedDblVerificationReqData = urlEncodeParam(encryptedDblVerificationReqData);
					
						if(encryptedDblVerificationReqData == null)
						{
								System.out.println("null value returned from urlEncodeParam! Please verify encodeDoubleVerificationReqData!");
						}

						encryptedDblVerificationReqData = "encdata=" + encryptedDblVerificationReqData + "&merchant_code=" + merchantCode;

						String dblVerificationEncryptedResp = httpClient.sendHttpsPost(dblVerificationURL, encryptedDblVerificationReqData);
						String dblVerificationDecryptedResp = decryptResponse(dblVerificationEncryptedResp);

						SBIPaymentResponseBean sbiPaymentDblVerificationResponseBean = new SBIPaymentResponseBean();
						populateResponseBean(dblVerificationDecryptedResp, sbiPaymentDblVerificationResponseBean);

						if(verifyCheckSums(dblVerificationDecryptedResp)) {
								return sbiPaymentDblVerificationResponseBean;
						}
						else {
								return null;
						}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				return null;
		}


		public static void main(String[] args)
		{
				int i =0;
				String feeAmount = null, merchantCode = null, paymentId = null, currencyType = "INR";

				while(i < args.length)
				{
						if(args[i].equals("-f"))
						{
								feeAmount = args[i+1].trim();
								i++;
						}
						else if(args[i].equals("-m"))
						{
								merchantCode = args[i+1].trim();
								i++;
						}
						else if(args[i].equals("-pid"))
						{
								paymentId = args[i+1].trim();
								i++;
						}
						else if( args[i].equals("-ctype") )
						{
							currencyType = args[i+1].trim();
							i++;
						}
						i++;
				}

				if(feeAmount == null || merchantCode == null || paymentId == null)
				{
						System.out.println("Usage: -f 1500 -pid 10000241 -m IIT_ROORKEE -ctype INR/USD");
						System.exit(0);
				}
				SBIOnlinePaymentResponse sbiPaymentResponse = new SBIOnlinePaymentResponse();
				SBIPaymentResponseBean response = sbiPaymentResponse.paymentDoubleVerification(feeAmount, paymentId, merchantCode, currencyType, "https://merchant.onlinesbi.com/thirdparties/doubleverification.htm");
				System.out.println( response.toString() );	
		}
}
