//@Author Tony Wagdi

#include "cryptopp/osrng.h"
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
	SecByteBlock key;
	SecByteBlock iv;
};
struct encodedData{
	string key;
	string iv;
	string cipher;
};
keyIV generateKeyIV()
{
	keyIV lockBox;
	AutoSeededRandomPool rand;
	SecByteBlock key(AES::MAX_KEYLENGTH); //Generate a secure key
	SecByteBlock iv(AES::MAX_KEYLENGTH); //Generate a secure initial vector
	//Seed random key and IV
	rand.GenerateBlock(key, key.size());
	rand.GenerateBlock(iv, iv.size());

	lockBox.key = key;
	lockBox.iv = iv;

	return lockBox;
}
string encryptLine(keyIV lockBox, string ptext)
{
	string cipher;
	//Encrypt the plaintext line
	try
	{
		GCM<AES>::Encryption encrypt;
		encrypt.SetKeyWithIV(lockBox.key, lockBox.key.size(), lockBox.iv, lockBox.iv.size());

		StringSource(ptext, true, new AuthenticatedEncryptionFilter(encrypt, new StringSink(cipher))); 
	}
	catch(const CryptoPP::Exception& e)
	{
		cerr << e.what() << endl;
		exit(FAIL);
	}
	return cipher;
}
encodedData encode(keyIV lockBox, string cipher)
{
	encodedData encoded;

	//Convert key to ASCII
	StringSource(lockBox.key, lockBox.key.size(), true, new HexEncoder(new StringSink(encoded.key)));
	//Convert iv to ASCII
	StringSource(lockBox.iv, lockBox.iv.size(), true, new HexEncoder(new StringSink(encoded.iv)));
	//Convert ciphertext to ASCII
	StringSource(cipher, true, new HexEncoder(new StringSink(encoded.cipher)));

	return encoded;
}
void encryptDocument(string pFile, string keyFile, string outFile)
{
	encodedData encoded;
	keyIV lockBox;
	string line;
	string cipher;
	ifstream file1(pFile.c_str());
	ofstream file2(keyFile.c_str());
	ofstream file3(outFile.c_str());

	if (!file1.good())
	{
		cerr << "File does not exist" << endl;
		exit(FAIL);
	}

	while (getline(file1, line))
	{
		lockBox = generateKeyIV();
		cipher = encryptLine(lockBox, line);
		encoded = encode(lockBox, cipher);
		//concat key with iv
		encoded.key += encoded.iv;
		//output to files
		file2 << encoded.key << endl;
		file3 << encoded.cipher << endl;
	}
	file3.close();
	file2.close();
	file1.close();
}
int main(int argc, char* argv[])
{
	string ptextFile;
	string keyFile;
	string outFile;

	if (argc != 4)
	{
		cerr << "Invalid number of arguments" << endl;
		exit(FAIL);
	}
	ptextFile = argv[1];
	keyFile = argv[2];
	outFile = argv[3];
	
	encryptDocument(ptextFile, keyFile, outFile);
	cout << "Encryption Successful" << endl;

	return SUCCESS;
}


