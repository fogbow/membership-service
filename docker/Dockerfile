FROM openjdk:8

ARG BRANCH_COMMON
ARG BRANCH_MS

# Install.
RUN \
  sed -i 's/# \(.*multiverse$\)/\1/g' /etc/apt/sources.list && \
  apt-get update -y && \
  apt-get upgrade -y && \
  apt-get install -y build-essential && \
  apt-get install -y software-properties-common && \
  apt-get install -y byobu curl git htop man unzip vim wget maven && \
  apt-get install -y net-tools iputils-ping && \
  rm -rf /var/lib/apt/lists/*

# Set environment variables.
ENV HOME /root

# Define working directory.
WORKDIR /root

# Installing Common
RUN \
  git clone https://github.com/fogbow/common.git && \
  (cd common && git checkout $BRANCH_COMMON && mvn install -Dmaven.test.skip=true)

# Installing Membership
RUN \
  git clone https://github.com/fogbow/membership-service.git && \
  (cd membership-service && git checkout $BRANCH_MS)

# Define working directory.
WORKDIR /root/membership-service

# Generates the build number based on the commit checksum
RUN \
    (ms_build_number=$(git rev-parse --short 'HEAD') && \
    cd ../common && common_build_number=$(git rev-parse --short 'HEAD') && \
    echo "build_number=ms-$build_number-common-$common_build_number" > build)

RUN \
  mvn dependency:sources

