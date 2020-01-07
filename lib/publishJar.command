mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file \
  -Dfile=/Users/aurora/Workspaces/equinox-workspace/EquinoxServerUtilities/equinoxServerUtilities.jar \
  -DgroupId=com.equinox \
  -DartifactId=serverUtilities \
  -Dversion=1.0.0 \
  -Dpackaging=jar \
  -DlocalRepositoryPath=/Users/aurora/Workspaces/equinox-workspace/EquinoxDataServer/lib

mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file \
  -Dfile=/Users/aurora/Documents/Developer/Libraries/base64-data-encryption/base64-all.jar \
  -DgroupId=com.base64 \
  -DartifactId=base64-all \
  -Dversion=1.0.0 \
  -Dpackaging=jar \
  -DlocalRepositoryPath=/Users/aurora/Workspaces/equinox-workspace/EquinoxDataServer/lib