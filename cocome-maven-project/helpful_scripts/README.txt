You need to specify the paths to the demanded files like settings.xml or pom.xml, glassfish etc.





########Linux#########


You might need to run sudo chmod +x on ALL scripts.

Further, you might need to run the scripts as sudo user
Important: When doing so, mvn downloads the dependencies into the .m2 folder of the root usr. This might take some time. But you can also copy the content of your local (user) .m2 folder into the root .m2.


You might run the dos2unix   command to convert line endings
 