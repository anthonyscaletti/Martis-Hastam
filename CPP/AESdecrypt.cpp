//@Author Tony Wagdi

#include <cryptopp/cryptlib.h>
#include <cryptopp/hex.h>
#include <cryptopp/filters.h>
#include <cryptopp/aes.h>
#include <cryptopp/gcm.h>
#include <cryptopp/secblock.h>
#include <fstream>
#include <iostream>
#include <string>
#include <cstdlib>

using namespace CryptoPP;
using namespace std;

#define SUCCESS 0
#define FAIL -1

struct keyIV{
	string key;
	string iv;
};
struct decodedData{
	string key;
	string iv;
	string cipher;
};
decodedData decode(keyIV lockBox, string cipher)
{
	decodedData decoded;

	//Decode the data
	StringSource(lockBox.key, true, new HexDecoder(new StringSink(decoded.key)));
	StringSource(lockBox.iv, true, new HexDecoder(new StringSink(decoded.iv)));
	StringSource(cipher, true, new HexDecoder(new StringSink(decoded.cipher)));

	return decoded;
}
string decryptLine(SecByteBlock key, SecByteBlock iv, string ctext)
{
	string ptext;
	//Decrypt the ciphertext line
	try
	{
		GCM<AES>::Decryption decrypt;
		decrypt.SetKeyWithIV(key, key.size(), iv, iv.size());

		StringSource s(ctext, true, new AuthenticatedDecryptionFilter(decrypt, new StringSink(ptext))); 
	}
	catch(const CryptoPP::Exception& e)
	{
		cerr << e.what() << endl;
		exit(FAIL);
	}
	return ptext;
}

void decryptDocument(string cfile, string keyFile, string outFile)
{
	decodedData decoded;
	keyIV lockBox;
	string line;
	string ptext;
	string keyIV;
	ifstream file1(cfile.c_str());
	ifstream file2(keyFile.c_str());
	ofstream file3(outFile.c_str());

	if (!file1.good() || !file2.good() || !file3.good())
	{
		cerr << "File does not exist" << endl;
		exit(FAIL);
	}

	while (getline(file1, line))
	{
		getline(file2, keyIV);
		//Seperate key from iv
		lockBox.iv = keyIV.substr(64, 130);
		lockBox.key = keyIV.substr(0, 64);
		//Decrypt line
		decoded = decode(lockBox, line);
		SecByteBlock key((const byte*)decoded.key.data(), AES::MAX_KEYLENGTH);
		SecByteBlock iv((const byte*)decoded.iv.data(), AES::MAX_KEYLENGTH);
		ptext = decryptLine(key, iv, decoded.cipher);
		//output to files
		file3 << ptext << endl;
	}
	file3.close();
	file2.close();
	file1.close();
}
int main(int argc, char* argv[])
{
	string keyFile;
	string cipherFile;
	string outFile;

	if (argc != 4)
	{
		cerr << "Invalid number of arguments" << endl;
		exit(FAIL);
	}

	cipherFile = argv[1];
	keyFile = argv[2];
	outFile = argv[3];

	decryptDocument(cipherFile, keyFile, outFile);
	cout << "Decryption Succesful" << endl;

	return SUCCESS;
}
