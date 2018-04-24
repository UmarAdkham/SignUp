-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 22, 2018 at 12:34 PM
-- Server version: 10.1.26-MariaDB
-- PHP Version: 7.1.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `new_jrlu`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `admin_username` varchar(40) NOT NULL,
  `admin_psw` varchar(50) NOT NULL,
  `admin_fs_name` varchar(50) NOT NULL,
  `admin_ls_name` varchar(50) NOT NULL,
  `bankID` varchar(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`admin_username`, `admin_psw`, `admin_fs_name`, `admin_ls_name`, `bankID`) VALUES
('alisherFayz', 'kukuku', 'Harry ', 'Potter', '2'),
('umarAdkham', 'password', 'Umar', 'Adkhamov', '1');

-- --------------------------------------------------------

--
-- Table structure for table `bank`
--

CREATE TABLE `bank` (
  `bankID` varchar(5) NOT NULL,
  `bankName` varchar(50) NOT NULL,
  `Address` varchar(50) NOT NULL,
  `zip_code` varchar(50) NOT NULL,
  `town` varchar(50) NOT NULL,
  `city` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bank`
--

INSERT INTO `bank` (`bankID`, `bankName`, `Address`, `zip_code`, `town`, `city`) VALUES
('1', 'Maybank', 'TTDI', '60000', 'Kuala Lumpur', 'Kuala Lumpur'),
('2', 'RHB', 'Damansara', '86000', 'Johor', 'Johor');

-- --------------------------------------------------------

--
-- Table structure for table `bank_customers`
--

CREATE TABLE `bank_customers` (
  `bankID` varchar(5) NOT NULL,
  `customerID` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `branch`
--

CREATE TABLE `branch` (
  `branchName` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `branch`
--

INSERT INTO `branch` (`branchName`) VALUES
('Kota Damansara'),
('Jalan Semantan'),
('Putrajaya'),
('KL Sentral'),
('One Utama'),
('Surian'),
('TTDI'),
('Subang'),
('Cyberjaya'),
('Kajang');

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `customerID` varchar(50) NOT NULL,
  `firstName` varchar(50) NOT NULL,
  `lastName` varchar(50) NOT NULL,
  `password` varchar(40) NOT NULL,
  `address` varchar(200) NOT NULL,
  `dob` varchar(20) NOT NULL,
  `passportNo` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`customerID`, `firstName`, `lastName`, `password`, `address`, `dob`, `passportNo`) VALUES
('rupini', 'Rupini', 'Chandran', '123', 'Ten Semantan, West Wing 12-04', '4/18/1998', 'AB0307689');

-- --------------------------------------------------------

--
-- Table structure for table `data_type`
--

CREATE TABLE `data_type` (
  `dataTypeID` int(10) NOT NULL,
  `fieldName` varchar(50) NOT NULL,
  `fieldType` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `data_type`
--

INSERT INTO `data_type` (`dataTypeID`, `fieldName`, `fieldType`) VALUES
(1, 'Firstname', 'Text Area'),
(2, 'Lastname', 'Text Area'),
(3, 'Passport Number', 'Text Area'),
(4, 'bnvv', 'Text Area'),
(5, 'jhbj', 'Text Area'),
(6, 'Firstname', 'Text Area'),
(7, 'Lastname', 'Text Area'),
(8, 'Passport Number', 'Text Area'),
(9, 'Email', 'Text Area'),
(10, 'Date of Birth', 'Date Picker'),
(11, 'Driver Licence Number', 'Text Area'),
(12, 'Firstname', 'Text Area'),
(13, 'Lastname', 'Text Area'),
(14, 'Passport Number', 'Text Area'),
(15, 'Address', 'Text Area'),
(16, 'Date of Birth', 'Date Picker'),
(17, 'Firstname', 'Text Area'),
(18, 'Firstname', 'Text Area'),
(19, 'Lastname', 'Text Area'),
(20, 'Company name', 'Text Area'),
(21, 'MyKad', 'Check Button'),
(22, 'Lastname', 'Text Area'),
(23, 'Lastname', 'Text Area'),
(24, 'Firstname', 'Text Area'),
(25, 'Lastname', 'Text Area'),
(26, 'Passport Number', 'Text Area'),
(27, 'Address', 'Text Area'),
(28, 'Date of Birth', 'Date Picker'),
(29, 'Firstname', 'Text Area'),
(30, 'Lastname', 'Text Area'),
(31, 'Passport Number', 'Text Area'),
(32, 'Address', 'Text Area'),
(33, 'Date of Birth', 'Date Picker'),
(34, 'Company registration number', 'Text Area'),
(35, 'Year of incorporation', 'Text Area');

-- --------------------------------------------------------

--
-- Table structure for table `filleddata`
--

CREATE TABLE `filleddata` (
  `serviceNo` varchar(5) NOT NULL,
  `customerID` varchar(5) NOT NULL,
  `data` varchar(50) NOT NULL,
  `dataValue` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `service`
--

CREATE TABLE `service` (
  `serviceID` int(10) NOT NULL,
  `serviceName` varchar(50) NOT NULL,
  `bankID` varchar(5) NOT NULL,
  `description` varchar(10000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `service`
--

INSERT INTO `service` (`serviceID`, `serviceName`, `bankID`, `description`) VALUES
(1, 'Advance Payment Guarantee', '1', 'asdas'),
(2, 'Smart Cash Extra Insurance', '1', 'Something'),
(3, 'Hire Purchase Loan', '2', 'Anything'),
(4, 'Opening Bank account', '2', ''),
(5, 'Term Loan Facility', '1', 'Something'),
(6, 'Residential Home Financing', '2', 'Something'),
(7, 'Standby Letter of Credit ', '1', 'Something'),
(8, 'Performance Guarantee', '2', 'Something');

-- --------------------------------------------------------

--
-- Table structure for table `service_data_type`
--

CREATE TABLE `service_data_type` (
  `serviceID` int(10) NOT NULL,
  `dataTypeID` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `service_data_type`
--

INSERT INTO `service_data_type` (`serviceID`, `dataTypeID`) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(2, 6),
(2, 7),
(2, 8),
(2, 9),
(2, 10),
(2, 11),
(3, 12),
(3, 13),
(3, 14),
(3, 15),
(3, 16),
(5, 17);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`admin_username`),
  ADD UNIQUE KEY `admin_username` (`admin_username`),
  ADD UNIQUE KEY `admin_fs_name_5` (`admin_fs_name`),
  ADD UNIQUE KEY `admin_username_2` (`admin_username`),
  ADD UNIQUE KEY `bankID_2` (`bankID`),
  ADD KEY `admin_fs_name_2` (`admin_fs_name`),
  ADD KEY `admin_fs_name_3` (`admin_fs_name`),
  ADD KEY `admin_fs_name_4` (`admin_fs_name`),
  ADD KEY `bankID` (`bankID`),
  ADD KEY `admin_fs_name` (`admin_fs_name`);

--
-- Indexes for table `bank`
--
ALTER TABLE `bank`
  ADD PRIMARY KEY (`bankID`),
  ADD UNIQUE KEY `bankID` (`bankID`);

--
-- Indexes for table `bank_customers`
--
ALTER TABLE `bank_customers`
  ADD PRIMARY KEY (`bankID`,`customerID`),
  ADD UNIQUE KEY `bankID` (`bankID`),
  ADD UNIQUE KEY `customerID` (`customerID`);

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`customerID`);

--
-- Indexes for table `data_type`
--
ALTER TABLE `data_type`
  ADD PRIMARY KEY (`dataTypeID`),
  ADD KEY `dataTypeID` (`dataTypeID`);

--
-- Indexes for table `filleddata`
--
ALTER TABLE `filleddata`
  ADD PRIMARY KEY (`serviceNo`),
  ADD UNIQUE KEY `serviceNo` (`serviceNo`),
  ADD UNIQUE KEY `customerID` (`customerID`),
  ADD KEY `serviceNo_2` (`serviceNo`),
  ADD KEY `customerID_2` (`customerID`);

--
-- Indexes for table `service`
--
ALTER TABLE `service`
  ADD PRIMARY KEY (`serviceID`),
  ADD UNIQUE KEY `serviceID` (`serviceID`),
  ADD KEY `bankID` (`bankID`);

--
-- Indexes for table `service_data_type`
--
ALTER TABLE `service_data_type`
  ADD PRIMARY KEY (`serviceID`,`dataTypeID`),
  ADD KEY `serviceNo` (`serviceID`),
  ADD KEY `dataTypeNo` (`dataTypeID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `data_type`
--
ALTER TABLE `data_type`
  MODIFY `dataTypeID` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;
--
-- AUTO_INCREMENT for table `service`
--
ALTER TABLE `service`
  MODIFY `serviceID` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `admin`
--
ALTER TABLE `admin`
  ADD CONSTRAINT `admin_ibfk_1` FOREIGN KEY (`bankID`) REFERENCES `bank` (`bankID`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `bank_customers`
--
ALTER TABLE `bank_customers`
  ADD CONSTRAINT `bank_customers_ibfk_1` FOREIGN KEY (`bankID`) REFERENCES `bank` (`bankID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `bank_customers_ibfk_2` FOREIGN KEY (`customerID`) REFERENCES `customer` (`customerID`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `filleddata`
--
ALTER TABLE `filleddata`
  ADD CONSTRAINT `filleddata_ibfk_2` FOREIGN KEY (`customerID`) REFERENCES `customer` (`customerID`) ON UPDATE NO ACTION;

--
-- Constraints for table `service_data_type`
--
ALTER TABLE `service_data_type`
  ADD CONSTRAINT `service_data_type_ibfk_1` FOREIGN KEY (`serviceID`) REFERENCES `service` (`serviceID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `service_data_type_ibfk_2` FOREIGN KEY (`dataTypeID`) REFERENCES `data_type` (`dataTypeID`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
