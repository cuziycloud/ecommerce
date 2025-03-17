import { Box, Typography, IconButton, Divider } from "@mui/material";
import { Facebook, Twitter, Instagram, YouTube } from "@mui/icons-material";
import React from "react";

const Footer = () => {
  return (
    <Box
      sx={{
        background: "linear-gradient(135deg, #1E1E1E, #3A3A3A)",
        color: "white",
        py: 6,
        px: { xs: 3, sm: 6, md: 12 },
      }}
    >
      {/* Container chính */}
      <Box
        sx={{
          display: "flex",
          flexWrap: "wrap",
          //justifyContent: { xs: "center", md: "space-between" }, 
          textAlign: { xs: "center", lg: "left" },
          gap: { xs: 4, sm: 6 },
        }}
      >
        {/* Cột 1 - Công ty */}
        <Box sx={{ flex: "1 1 200px" }}>
          <Typography variant="h6" fontWeight="bold" gutterBottom>
            Company
          </Typography>
          <Box display="flex" flexDirection="column" gap={1}>
            <Typography variant="body2" sx={{ opacity: 0.8 }}>
              About Us
            </Typography>
            <Typography variant="body2" sx={{ opacity: 0.8 }}>
              Careers
            </Typography>
            <Typography variant="body2" sx={{ opacity: 0.8 }}>
              Blog
            </Typography>
          </Box>
        </Box>

        {/* Cột 2 - Hỗ trợ khách hàng */}
        <Box sx={{ flex: "1 1 200px" }}>
          <Typography variant="h6" fontWeight="bold" gutterBottom>
            Support
          </Typography>
          <Box display="flex" flexDirection="column" gap={1}>
            <Typography variant="body2" sx={{ opacity: 0.8 }}>
              Contact Us
            </Typography>
            <Typography variant="body2" sx={{ opacity: 0.8 }}>
              Help Center
            </Typography>
            <Typography variant="body2" sx={{ opacity: 0.8 }}>
              Shipping & Returns
            </Typography>
          </Box>
        </Box>

        {/* Cột 3 - Chính sách */}
        <Box sx={{ flex: "1 1 200px" }}>
          <Typography variant="h6" fontWeight="bold" gutterBottom>
            Legal
          </Typography>
          <Box display="flex" flexDirection="column" gap={1}>
            <Typography variant="body2" sx={{ opacity: 0.8 }}>
              Privacy Policy
            </Typography>
            <Typography variant="body2" sx={{ opacity: 0.8 }}>
              Terms of Service
            </Typography>
            <Typography variant="body2" sx={{ opacity: 0.8 }}>
              Refund Policy
            </Typography>
          </Box>
        </Box>

        {/* Cột 4 - Mạng xã hội */}
        <Box
          sx={{ flex: "1 1 200px", textAlign: { xs: "center", lg: "left" } }}
        >
          <Typography variant="h6" fontWeight="bold" gutterBottom>
            Follow Us
          </Typography>
          <Box
            display="flex"
            justifyContent={{ xs: "center", lg: "left" }}
            gap={2}
          >
            <IconButton
              sx={{ color: "white", "&:hover": { color: "#1877F2" } }}
            >
              <Facebook />
            </IconButton>
            <IconButton
              sx={{ color: "white", "&:hover": { color: "#1DA1F2" } }}
            >
              <Twitter />
            </IconButton>
            <IconButton
              sx={{ color: "white", "&:hover": { color: "#E4405F" } }}
            >
              <Instagram />
            </IconButton>
            <IconButton
              sx={{ color: "white", "&:hover": { color: "#FF0000" } }}
            >
              <YouTube />
            </IconButton>
          </Box>
        </Box>
      </Box>

      {/* Đường phân cách */}
      <Divider sx={{ bgcolor: "gray", my: 4 }} />

      {/* Bản quyền */}
      <Typography variant="body2" align="center" sx={{ opacity: 0.7 }}>
        © 2024 YourCompany. All rights reserved.
      </Typography>
    </Box>
  );
};

export default Footer;
