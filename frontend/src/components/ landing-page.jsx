"use client";

import { useEffect, useState } from "react";
import Image from "next/image";
import { cn } from "../lib/utils";

export default function LandingPage() {
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const [currentTextIndex, setCurrentTextIndex] = useState(0);
  const [textVisible, setTextVisible] = useState(true);

  // Using the provided image URLs
  const images = [
    "https://iutoic-dhaka.edu/uploads/img/1705465958_1201.jpg",
    "https://iutoic-dhaka.edu/uploads/img/1705465851_1711.jpg",
    "https://iutoic-dhaka.edu/uploads/img/1600322507_1803.jpg",
    "https://iutoic-dhaka.edu/uploads/img/1553676299.jpg",
  ];

  const texts = ["Welcome to IUT Study Sync", "Best Student environment"];

  useEffect(() => {
    // Image slideshow timer
    const imageInterval = setInterval(() => {
      setCurrentImageIndex((prevIndex) => (prevIndex + 1) % images.length);
    }, 5000);

    // Text animation timer
    const textInterval = setInterval(() => {
      setTextVisible(false);

      setTimeout(() => {
        setCurrentTextIndex((prevIndex) => (prevIndex + 1) % texts.length);
        setTextVisible(true);
      }, 1000);
    }, 4000);

    return () => {
      clearInterval(imageInterval);
      clearInterval(textInterval);
    };
  }, [images.length, texts.length]);

  return (
    <div className="relative h-[calc(100vh-64px)] w-full overflow-hidden">
      {/* Image Slideshow */}
      {images.map((src, index) => (
        <div
          key={index}
          className={cn(
            "absolute inset-0 transition-opacity duration-1000",
            currentImageIndex === index ? "opacity-100" : "opacity-0"
          )}
        >
          <div className="relative w-full h-full">
            {/* Using next/image with unoptimized to handle external images */}
            <Image
              src={src || "/placeholder.svg"}
              alt={`IUT Campus Slide ${index + 1}`}
              fill
              unoptimized
              priority={index === 0}
              className="object-cover"
              onError={(e) => {
                // Fallback to placeholder if image fails to load
                e.target.src = `/placeholder.svg?height=1080&width=1920&text=Image+${
                  index + 1
                }`;
              }}
            />
          </div>
        </div>
      ))}

      {/* Overlay with gradient */}
      <div className="absolute inset-0 bg-gradient-to-b from-black/50 to-black/70" />

      {/* Text Animation */}
      <div className="absolute inset-0 flex flex-col items-center justify-center text-center">
        <div className="relative h-32 overflow-hidden">
          <h1
            className={cn(
              "text-4xl md:text-6xl font-bold text-white transition-all duration-1000",
              textVisible
                ? "translate-y-0 opacity-100"
                : "translate-y-16 opacity-0"
            )}
          >
            {texts[currentTextIndex]}
          </h1>
        </div>

        <div className="mt-8">
          <button className="bg-white text-black px-6 py-3 rounded-full font-medium hover:bg-opacity-90 transition-colors">
            Get Started
          </button>
        </div>
      </div>
    </div>
  );
}
