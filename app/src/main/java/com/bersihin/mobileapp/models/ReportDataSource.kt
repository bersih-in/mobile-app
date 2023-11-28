package com.bersihin.mobileapp.models

object ReportDataSource {
    val reports = listOf(
        Report(
            "1",
            "Spotted Trash Pile at Bandung",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam placerat ex et sapien mollis efficitur. Nunc non vulputate nisi. Duis elementum arcu et risus semper mattis. Quisque ornare tellus vitae fermentum rutrum. In mi quam, venenatis sit amet hendrerit eget, ultricies at odio. Sed in purus purus. ",
            "https://i.ibb.co/0jZGZJd/IMG-20211017-162547.jpg",
            -6.1753924,
            106.8249641,
            ReportStatus.PENDING,
            null,
            null
        ),
        Report(
            "2",
            "Spotted Trash Pile at Bandung",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam placerat ex et sapien mollis efficitur. Nunc non vulputate nisi. Duis elementum arcu et risus semper mattis. Quisque ornare tellus vitae fermentum rutrum. In mi quam, venenatis sit amet hendrerit eget, ultricies at odio. Sed in purus purus. ",
            "https://i.ibb.co/0jZGZJd/IMG-20211017-162547.jpg",
            -6.1753924,
            106.8249641,
            ReportStatus.VERIFIED,
            null,
            null
        ),
        Report(
            "3",
            "Spotted Trash Pile at Bandung",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam placerat ex et sapien mollis efficitur. Nunc non vulputate nisi. Duis elementum arcu et risus semper mattis. Quisque ornare tellus vitae fermentum rutrum. In mi quam, venenatis sit amet hendrerit eget, ultricies at odio. Sed in purus purus. ",
            "https://i.ibb.co/0jZGZJd/IMG-20211017-162547.jpg",
            -6.1753924,
            106.8249641,
            ReportStatus.REJECTED_BY_SYSTEM,
            "Gambar tidak jelas",
            null
        ),
        Report(
            "4",
            "Spotted Trash Pile at Bandung",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam placerat ex et sapien mollis efficitur. Nunc non vulputate nisi. Duis elementum arcu et risus semper mattis. Quisque ornare tellus vitae fermentum rutrum. In mi quam, venenatis sit amet hendrerit eget, ultricies at odio. Sed in purus purus. ",
            "https://i.ibb.co/0jZGZJd/IMG-20211017-162547.jpg",
            -6.1753924,
            106.8249641,
            ReportStatus.REJECTED_BY_ADMIN,
            "Gambar tidak jelas",
            null
        ),
        Report(
            "5",
            "Spotted Trash Pile at Bandung",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam placerat ex et sapien mollis efficitur. Nunc non vulputate nisi. Duis elementum arcu et risus semper mattis. Quisque ornare tellus vitae fermentum rutrum. In mi quam, venenatis sit amet hendrerit eget, ultricies at odio. Sed in purus purus. ",
            "https://i.ibb.co/0jZGZJd/IMG-20211017-162547.jpg",
            -6.1753924,
            106.8249641,
            ReportStatus.REJECTED_BY_WORKER,
            "Gambar tidak jelas",
            null
        )
    )
}